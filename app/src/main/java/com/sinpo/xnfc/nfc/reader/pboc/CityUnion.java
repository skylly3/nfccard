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
//城市一卡通 包含了长安通功能
package com.sinpo.xnfc.nfc.reader.pboc;

//import java.io.IOException;

import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
//import com.sinpo.xnfc.nfc.bean.Card;
//import com.sinpo.xnfc.nfc.reader.pboc.StandardPboc.HINT;
import com.sinpo.xnfc.nfc.tech.Iso7816;

import android.annotation.SuppressLint;

final class CityUnion extends StandardPboc {
	private Object applicationId = SPEC.APP.UNKNOWN;

	@Override
	protected Object getApplicationId() {
		return applicationId;
	}

	@Override
	protected byte[] getMainApplicationId() {
		return new byte[] { (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03,
				(byte) 0x86, (byte) 0x98, (byte) 0x07, (byte) 0x01, };
	}
	
//	@Override
//	protected boolean resetTag(Iso7816.StdTag tag) throws IOException {
//		return tag.selectByID(DFI_MF).isOkey() || tag.selectByName(DFN_PSE).isOkey();
//	}
	
//	@Override
//	protected HINT readCard(Iso7816.StdTag tag, Card card) throws IOException {
//
//		Iso7816.Response INFO, BALANCE;
//		
//		for (int i = 0; i <= 5; i ++)
//		{
//			INFO = tag.readBinary(i);
//			if (INFO.isOkey())
//			{
//				int b =3;
//				b =b;
//			}
//		}
//		
//	 	final byte[] aaa = { (byte) 0x32, (byte) 0x31 };
//		
//	   INFO = tag.selectByID(aaa);
//		
//		if (!selectMainApplication(tag))
//		{
//			return HINT.GONEXT;
//		}
//		INFO = tag.readBinary(SFI_EXTRA);
//		
//		return HINT.GONEXT;
// 
//	
//	}
	
	@SuppressLint("DefaultLocale")
	@Override
	protected void parseInfo21(Application app, Iso7816.Response data, int dec, boolean bigEndian) {

		if (!data.isOkey() || data.size() < 30) {
			return;
		}

		final byte[] d = data.getBytes();

		if (d[2] == 0x20 && d[3] == 0x00) {
			applicationId = SPEC.APP.SHANGHAIGJ;
			bigEndian = true;
		} else if (d[2] == 0x71 && d[3] == 0x00) {
			applicationId = SPEC.APP.CHANGANTONG;
			bigEndian = false;
		} else {
			applicationId = SPEC.getCityUnionCardNameByZipcode(Util.toHexString(d[2], d[3]));
			bigEndian = false;
		}

		if (dec < 1 || dec > 10) {
			app.setProperty(SPEC.PROP.SERIAL, Util.toHexString(d, 10, 10));
		} else {
		//	final int sn = Util.toInt(d, 20 - dec, dec);
		//	final String ss = bigEndian ? Util.toStringR(sn) : String.format("%d", 0xFFFFFFFFL & sn);
			
			//modify by jl,解决长沙城市一卡通读出的卡号不正确的问题,2018.9.12
			String ss = Util.toHexString(d, 12, 8);
			
			
			app.setProperty(SPEC.PROP.SERIAL, ss);
		}

		if (d[9] != 0)
			app.setProperty(SPEC.PROP.VERSION, String.valueOf(d[9]));

		app.setProperty(SPEC.PROP.DATE, String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X",
				d[20], d[21], d[22], d[23], d[24], d[25], d[26], d[27]));
	}
}
