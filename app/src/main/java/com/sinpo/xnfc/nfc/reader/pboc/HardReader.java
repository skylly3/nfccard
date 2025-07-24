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

//通用阅读方法
package com.sinpo.xnfc.nfc.reader.pboc;

import java.io.IOException;
import java.util.ArrayList;

import com.sinpo.xnfc.SPEC;
//import com.sinpo.xnfc.nfc.Util;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;
import com.sinpo.xnfc.nfc.tech.Iso7816;
import com.sinpo.xnfc.nfc.tech.Iso7816.BerT;

final class HardReader extends StandardPboc {
	public static final byte TMPL_PDR = 0x70; // Payment Directory Entry Record
	public static final byte TMPL_PDE = 0x61; // Payment Directory Entry
	
	@Override
	protected SPEC.APP getApplicationId() {
		return SPEC.APP.UNKNOWN;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HINT readCard(Iso7816.StdTag tag, Card card) throws IOException {

		Iso7816.Response INFO, BALANCE;

		/*--------------------------------------------------------------*/
		// read balance
		/*--------------------------------------------------------------*/
		BALANCE = getBalance(tag);

		INFO = null;
		ArrayList<byte[]> LOG = new ArrayList<byte[]>();
		byte[] name = null;

		/*--------------------------------------------------------------*/
		// try to find AID list
		/*--------------------------------------------------------------*/
		ArrayList<byte[]> AIDs = findAIDs(tag);
		for (final byte[] aid : AIDs) {

			/*--------------------------------------------------------------*/
			// select Main Application
			/*--------------------------------------------------------------*/
			if ((name = selectAID(tag, aid)) != null) {
				/*--------------------------------------------------------------*/
				// read balance
				/*--------------------------------------------------------------*/
				if (!BALANCE.isOkey())
					BALANCE = getBalance(tag);

				/*--------------------------------------------------------------*/
				// read card info file, binary (21)
				/*--------------------------------------------------------------*/
				if (INFO == null || !INFO.isOkey())
					INFO = tag.readBinary(SFI_EXTRA);

				/*--------------------------------------------------------------*/
				// read log file, record (24)
				/*--------------------------------------------------------------*/
				LOG.addAll(readLog24(tag, SFI_LOG));
			}
		}

		/*--------------------------------------------------------------*/
		// try to PXX AID
		/*--------------------------------------------------------------*/
		if ((INFO == null || !INFO.isOkey())
				&& ((name = selectAID(tag, DFN_PXX)) != null)) {

			if (!BALANCE.isOkey())
				BALANCE = getBalance(tag);

			INFO = tag.readBinary(SFI_EXTRA);
			LOG.addAll(readLog24(tag, SFI_LOG));
		}

		/*--------------------------------------------------------------*/
		// try to 0x1001 AID
		/*--------------------------------------------------------------*/
		if ((INFO == null || !INFO.isOkey()) && tag.selectByID(DFI_EP).isOkey()) {
			name = DFI_EP;

			if (!BALANCE.isOkey())
				BALANCE = getBalance(tag);

			INFO = tag.readBinary(SFI_EXTRA);
			LOG.addAll(readLog24(tag, SFI_LOG));
		}

		if (!BALANCE.isOkey() && (INFO == null || !INFO.isOkey()) && LOG.isEmpty() && name == null)
			return null;

		/*--------------------------------------------------------------*/
		// build result
		/*--------------------------------------------------------------*/
		final Application app = createApplication();

		parseBalance(app, BALANCE);

		parseInfo5(app, INFO);

		parseLog24(app, LOG);

		configApplication(app);

		card.addApplication(app);

		return HINT.STOP;
	}

	private static byte[] selectAID(Iso7816.StdTag tag, byte[] aid) throws IOException {
		if (!tag.selectByName(DFN_PSE).isOkey()
				&& !tag.selectByID(DFI_MF).isOkey())
			return null;

		final Iso7816.Response rsp = tag.selectByName(aid);
		if (!rsp.isOkey())
			return null;

		Iso7816.BerTLV tlv = Iso7816.BerTLV.read(rsp);
		if (tlv.t.match(Iso7816.BerT.TMPL_FCI)) {
			tlv = tlv.getChildByTag(Iso7816.BerT.CLASS_DFN);
			if (tlv != null)
				return tlv.v.getBytes();
		}

		return aid;
	}

	private static ArrayList<byte[]> findAIDs(Iso7816.StdTag tag) throws IOException {
		ArrayList<byte[]> ret = new ArrayList<byte[]>();

		for (int i = 1; i <= 31; ++i) {
			Iso7816.Response r = tag.readRecord(i, 1);
			for (int p = 2; r.isOkey(); ++p) {
				byte[] aid = findAID(r);
				if (aid == null)
					break;

				ret.add(aid);
				r = tag.readRecord(i, p);
			}
		}

		return ret;
	}

	private static byte[] findAID(Iso7816.Response record) {
		Iso7816.BerTLV tlv = Iso7816.BerTLV.read(record);
		if (tlv.t.match(TMPL_PDR)) {
			tlv = tlv.getChildByTag(BerT.CLASS_ADO);
			if (tlv != null) {
				tlv = tlv.getChildByTag(BerT.CLASS_AID);

				return (tlv != null) ? tlv.v.getBytes() : null;
			}
		}
		return null;
	}

	private static Iso7816.Response getBalance(Iso7816.StdTag tag) throws IOException {
		final Iso7816.Response rsp = tag.getBalance(0, true);
		return rsp.isOkey() ? rsp : tag.getBalance(0, false);
	}
	
	private void parseInfo5(Application app, Iso7816.Response info) {
		if (info.size() < 27) {
			return;
		}

		final byte[] d = info.getBytes();
		//app.setProperty(SPEC.PROP.SERIAL, Util.toHexString(sn.getBytes(), 0, 5));
		app.setProperty(SPEC.PROP.VERSION, String.format("%02d", d[24]));
		app.setProperty(SPEC.PROP.DATE, String.format(
				"%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20], d[21], d[22],
				d[23], d[16], d[17], d[18], d[19]));
	}
}
