/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.sinpo.xnfc.nfc.reader;

import java.io.IOException;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;
import com.sinpo.xnfc.nfc.tech.FeliCa;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.NfcF;

final class FelicaReader {

	static void readCard(Context ctx, Tag tagx, Card card) throws IOException {

		final NfcF nfcf = NfcF.get(tagx);
		if (nfcf == null)
			return;

		final FeliCa.Tag felica = new FeliCa.Tag(nfcf);

		felica.connect();

		// FeliCa.SystemCode systems[] = tag.getSystemCodeList();
		/*
		 * if (systems.length == 0) { systems = new FeliCa.SystemCode[] { new
		 * FeliCa.SystemCode( tag.getSystemCodeByte()) }; }
		 * 
		 * for (final FeliCa.SystemCode sys : systems)
		 * card.addApplication(readApplication(tag, sys.toInt()));
		 */

		final int systemx = felica.getSystemCode();
 
		// 八达通
		try {// better old card compatibility
			card.addApplication(readApplication(ctx, tagx, felica, SYS_OCTOPUS));
			if (felica.isConnected())
				felica.close();
			return;
		} catch (IOException e) {
			System.out.println("not octopus");
		}
		
		// 下面的代码对我的深圳通不支持,暂时屏蔽
		// 深圳通
		
		try {
			Application app = readApplication(ctx, tagx, felica, SYS_SZT);
			if (app != null) {// 读卡成功
				card.addApplication(app);
				if (felica.isConnected())
					felica.close();
				return;
			}
		} catch (IOException e) {
			System.out.println("not szt");
		}
      
		PukaCardOperator operater = new PukaCardOperator(tagx, felica);
		try {
			if (operater.openChanel(nfcf)) {
				if (felica != null) {
					Application app;
					app = new Application();
					app.setProperty(SPEC.PROP.ID, SPEC.APP.SHENZHENTONG_OLD);
					app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
					// final FeliCa.ServiceCode scode = new
					// FeliCa.ServiceCode(SRV_SZT);
					felica.polling(SYS_SZT);

					// app.setProperty(SPEC.PROP.SERIAL,
					// felica.getIDm().toString());
					// app.setProperty(SPEC.PROP.PARAM,
					// felica.getPMm().toString());

					boolean bRet = operater.checkNfcFCardResult(app, ctx);
					if (bRet)
						card.addApplication(app);

				} else if (!operater.PukaRechargeCheck(true)) {// isodep卡
																// return null;
				}
				operater.closeChanel();
				// MainActivity.this.mhandler.sendEmptyMessage(0);
				// return;
			} // if (operater.openChanel())
		} // try
		catch (Exception localException) {
			localException.printStackTrace();
			// MainActivity.this.mhandler.sendEmptyMessage(1);
			// return null;
		}
		if (felica.isConnected())
			felica.close();
	}

	private static final int SYS_SZT = 0x8005;
	private static final int SYS_OCTOPUS = 0x8008;

	private static final int SRV_SZT = 0x0118;
	private static final int SRV_OCTOPUS = 0x0117;

	private static Application readApplication(Context ctx, Tag tagx, FeliCa.Tag felica, int system)
			throws IOException {

		final FeliCa.ServiceCode scode;
		final Application app;

		if (system == SYS_OCTOPUS) {
			app = new Application();
			app.setProperty(SPEC.PROP.ID, SPEC.APP.OCTOPUS);
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.HKD);
			scode = new FeliCa.ServiceCode(SRV_OCTOPUS);
		} else if (system == SYS_SZT) {
			app = new Application();
			app.setProperty(SPEC.PROP.ID, SPEC.APP.SHENZHENTONG);
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
			scode = new FeliCa.ServiceCode(SRV_SZT);
		} else {
			return null;
		}

		app.setProperty(SPEC.PROP.SERIAL, felica.getIDm().toString());
		app.setProperty(SPEC.PROP.PARAM, felica.getPMm().toString());

		felica.polling(system);

		final float[] data = new float[] { 0, 0, 0 };

		int p = 0;

		try {
			for (byte i = 0; p < data.length; ++i) {
				final FeliCa.ReadResponse r = felica.readWithoutEncryption(scode, i);
				if (!r.isOkey())
					break;

				data[p++] = (Util.toInt(r.getBlockData(), 0, 4) - 350) / 10.0f;
			} // for
		} // try
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (p != 0)
			app.setProperty(SPEC.PROP.BALANCE, parseBalance(data));
		else
			app.setProperty(SPEC.PROP.BALANCE, Float.NaN);

		return app;
	}

	private static float parseBalance(float[] value) {
		float balance = 0f;

		for (float v : value)
			balance += v;

		return balance;
	}

}
