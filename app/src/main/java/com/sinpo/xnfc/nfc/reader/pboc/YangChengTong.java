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

package com.sinpo.xnfc.nfc.reader.pboc;

import java.io.IOException;
import java.util.ArrayList;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;
//import com.sinpo.xnfc.nfc.reader.pboc.StandardPboc.HINT;
import com.sinpo.xnfc.nfc.tech.Iso7816;

final class YangchengTong extends StandardPboc {
	//PAY.APPY
	private final static byte[] DFN_SRV = { (byte) 'P', (byte) 'A', (byte) 'Y',
			(byte) '.', (byte) 'A', (byte) 'P', (byte) 'P', (byte) 'Y', };

	//PAY.PASD
	private final static byte[] DFN_SRV_S1 = { (byte) 'P', (byte) 'A',
			(byte) 'Y', (byte) '.', (byte) 'P', (byte) 'A', (byte) 'S',
			(byte) 'D', };

	//PAY.TICL
	private final static byte[] DFN_SRV_S2 = { (byte) 'P', (byte) 'A',
			(byte) 'Y', (byte) '.', (byte) 'T', (byte) 'I', (byte) 'C',
			(byte) 'L', };
	@Override
	protected SPEC.APP getApplicationId() {
		return SPEC.APP.YANGCHENGTONG;
	}

	@Override
	protected byte[] getMainApplicationId() {
		return DFN_SRV;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HINT readCard(Iso7816.StdTag tag, Card card) throws IOException {

		Iso7816.Response INFO, BALANCE;

		/*--------------------------------------------------------------*/
		// select Main Application select ep
		/*--------------------------------------------------------------*/
		if (!selectMainApplication(tag))
			return HINT.GONEXT;

		/*--------------------------------------------------------------*/
		// read card info file, binary (21)
		/*--------------------------------------------------------------*/
		INFO = tag.readBinary(SFI_EXTRA);
	
		/*--------------------------------------------------------------*/
		// read balance 读取余额
		/*--------------------------------------------------------------*/
		tag.selectByName(DFN_SRV_S2);
		BALANCE = tag.getBalance(0, true);
		
		/*--------------------------------------------------------------*/
		// read log file, record (24) 读交易明细 （0x18）
		/*--------------------------------------------------------------*/
	    ArrayList<byte[]> LOG1 = (tag.selectByName(DFN_SRV_S1).isOkey()) ? readLog24(tag, SFI_LOG) : null;

		ArrayList<byte[]> LOG2 = (tag.selectByName(DFN_SRV_S2).isOkey()) ? readLog24(tag, SFI_LOG) : null;
			

		/*--------------------------------------------------------------*/
		// build result
		/*--------------------------------------------------------------*/
		final Application app = createApplication();

		parseBalance(app, BALANCE);

		parseInfo21(app, INFO);

		parseLog24(app, LOG1, LOG2);

		configApplication(app);

		card.addApplication(app);

		return HINT.STOP;
	}

	//private final static int SFI_INFO = 5;
	//private final static int SFI_SERL = 10;

	private void parseInfo21(Application app, Iso7816.Response info) {
		if (!info.isOkey() || info.size() < 50) {
			return;
		}
	 
		final byte[] d = info.getBytes();
		app.setProperty(SPEC.PROP.SERIAL, Util.toHexString(d, 11, 5));
		app.setProperty(SPEC.PROP.VERSION, String.format("%02X.%02X", d[44], d[45]));
		app.setProperty(SPEC.PROP.DATE, String.format(
				"%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[23],
				d[24], d[25], d[26], d[27], d[28], d[29], d[30]));
	}
}
