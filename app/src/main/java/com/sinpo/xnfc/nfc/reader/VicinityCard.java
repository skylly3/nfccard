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

//深圳图书馆卡
import android.content.res.Resources;
import android.nfc.tech.NfcV;

import com.sinpo.xnfc.R;
//import com.sinpo.xnfc.Util;
import com.sinpo.xnfc.SPEC;
import com.sinpo.xnfc.nfc.bean.Application;
import com.sinpo.xnfc.nfc.bean.Card;

final class VicinityCard {
	private static final int SYS_UNKNOWN = 0x00000000;
	private static final int SYS_SZLIB = 0x00010000;
	private static final int DEP_SZLIB_CENTER = 0x0100;
	private static final int DEP_SZLIB_NANSHAN = 0x0200;

	private static final int SRV_USER = 0x0001;
	private static final int SRV_BOOK = 0x0002;

	public static final int SW1_OK = 0x00;

	private static String ByteArrayToHexString(byte[] inarray) { // converts
																	// byte
		// arrays to string
		int i, j, in;
		String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		String out = "";

		for (j = 0; j < inarray.length; ++j) {
			in = inarray[j] & 0xff;
			i = (in >> 4) & 0x0f;
			out += hex[i];
			i = in & 0x0f;
			out += hex[i];
		}
		return out;
	}

	static void readCard(NfcV tech, Card card) {
		// String data = null;
		try {
			tech.connect();

			int pos, BLKSIZE, BLKCNT;
			byte cmd[], rsp[], ID[], RAW[], STA[], flag, DSFID;

			ID = tech.getTag().getId();
			if (ID == null || ID.length != 8)
				throw new Exception();

			/*--------------------------------------------------------------*/
			// get system information
			/*--------------------------------------------------------------*/
			cmd = new byte[10];
			cmd[0] = (byte) 0x22; // flag
			cmd[1] = (byte) 0x2B; // command
			System.arraycopy(ID, 0, cmd, 2, ID.length); // UID

			rsp = tech.transceive(cmd);
			if (rsp[0] != SW1_OK)
				throw new Exception();

			pos = 10;
			flag = rsp[1];

			DSFID = ((flag & 0x01) == 0x01) ? rsp[pos++] : 0;

			if ((flag & 0x02) == 0x02)
				pos++;

			if ((flag & 0x04) == 0x04) {
				BLKCNT = rsp[pos++] + 1;
				BLKSIZE = (rsp[pos++] & 0xF) + 1;
			} else {
				BLKCNT = BLKSIZE = 0;
			}

			/*--------------------------------------------------------------*/
			// read first 8 block
			/*--------------------------------------------------------------*/
			cmd = new byte[12];
			cmd[0] = (byte) 0x22; // flag
			cmd[1] = (byte) 0x23; // command
			System.arraycopy(ID, 0, cmd, 2, ID.length); // UID
			cmd[10] = (byte) 0x00; // index of first block to get
			cmd[11] = (byte) 0x07; // block count, one less! (see ISO15693-3)

			rsp = tech.transceive(cmd);
			if (rsp[0] != SW1_OK)
				throw new Exception();

			RAW = rsp;

			/*--------------------------------------------------------------*/
			// read last block
			/*--------------------------------------------------------------*/
			cmd[10] = (byte) (BLKCNT - 1); // index of first block to get
			cmd[11] = (byte) 0x00; // block count, one less! (see ISO15693-3)

			rsp = tech.transceive(cmd);
			if (rsp[0] != SW1_OK)
				throw new Exception();

			STA = rsp;

			// String data = ByteArrayToHexString(rsp);

			/*--------------------------------------------------------------*/
			// build result string
			/*--------------------------------------------------------------*/
			final int type = parseType(DSFID, RAW, BLKSIZE);
			final String name = parseName(type);
			final String info = parseInfo(ID);
			final String extra = parseData(type, RAW, STA, BLKSIZE);
			
			final Application app;
			app = new Application();
			app.setProperty(SPEC.PROP.ID, name);
			app.setProperty(SPEC.PROP.CURRENCY, SPEC.CUR.CNY);
			app.setProperty(SPEC.PROP.SERIAL, info);
			app.setProperty(SPEC.PROP.PARAM, extra);
			card.addApplication(app);

		} catch (Exception e) {
			// data = null;
			// data = e.getMessage();
		}

		try {
			tech.close();
		} catch (Exception e) {
		}

		// return data;
	}

	private static int parseType(byte dsfid, byte[] raw, int blkSize) {
		int ret = SYS_UNKNOWN;
		if (blkSize == 4 && (raw[4] & 0x10) == 0x10 && (raw[14] & 0xAB) == 0xAB && (raw[13] & 0xE0) == 0xE0) {
			ret = SYS_SZLIB;

			if ((raw[13] & 0x0F) == 0x05)
				ret |= DEP_SZLIB_CENTER;
			else
				ret |= DEP_SZLIB_NANSHAN;

			if (raw[4] == 0x12)
				ret |= SRV_USER;
			else
				ret |= SRV_BOOK;

		}
		return ret;
	}

	private static String parseName(int type) {
		if ((type & SYS_SZLIB) == SYS_SZLIB) {
			final Object dep;
			if ((type & DEP_SZLIB_CENTER) == DEP_SZLIB_CENTER)
				dep =  (R.string.name_szlib_center);
			else if ((type & DEP_SZLIB_NANSHAN) == DEP_SZLIB_NANSHAN)
				dep = (R.string.name_szlib_nanshan);
			else
				dep = null;

			final Object srv;
			if ((type & SRV_BOOK) == SRV_BOOK)
				srv = (R.string.name_lib_booktag);
			else if ((type & SRV_USER) == SRV_USER)
				srv = (R.string.name_lib_readercard);
			else
				srv = null;

			if (dep != null && srv != null)
				return dep.toString() + " " + srv.toString();
		}
		Object oRet = (R.string.name_unknowntag);
		return oRet.toString();
	}

	private static String parseInfo(byte[] id) {
		final StringBuilder r = new StringBuilder();
		final Object i = (R.string.lab_id);
		r.append("<b>").append(i.toString()).append("</b> ").append(ByteArrayToHexString(id));

		return r.toString();
	}

	private static String parseData(int type, byte[] raw, byte[] sta, int blkSize) {
		if ((type & SYS_SZLIB) == SYS_SZLIB) {
			return parseSzlibData(type, raw, sta, blkSize);
		}
		return null;
	}

	private static String parseSzlibData(int type, byte[] raw, byte[] sta, int blkSize) {

		long id = 0;
		for (int i = 3; i > 0; --i)
			id = (id <<= 8) | (0x000000FF & raw[i]);

		for (int i = 8; i > 4; --i)
			id = (id <<= 8) | (0x000000FF & raw[i]);

		final Object sid;
		if ((type & SRV_USER) == SRV_USER)
			sid = (R.string.lab_user_id);
		else
			sid = (R.string.lab_bktg_sn);

		final StringBuilder r = new StringBuilder();
		r.append("<b>").append(sid.toString()).append(" <font color=\"teal\">");
		r.append(String.format("%013d", id)).append("</font></b><br />");

		final Object scat;
		if ((type & SRV_BOOK) == SRV_BOOK) {
			final byte cat = raw[12];
			if ((type & DEP_SZLIB_NANSHAN) == DEP_SZLIB_NANSHAN) {
				if (cat == 0x10)
					scat = (R.string.name_bkcat_soc);
				else if (cat == 0x20) {
					if (raw[11] == (byte) 0x84)
						scat = (R.string.name_bkcat_ltr);
					else
						scat = (R.string.name_bkcat_sci);
				} else
					scat = null;
			} else {
				scat = null;
			}

			if (scat != null) {
				final Object scl = (R.string.lab_bkcat);
				r.append("<b>").append(scl.toString()).append("</b> ").append(scat.toString()).append("<br />");
			}
		}

		// final int len = raw.length;
		// for (int i = 1, n = 0; i < len; i += blkSize) {
		// final String blk = Util.toHexString(raw, i, blkSize);
		// r.append("<br />").append(n++).append(": ").append(blk);
		// }

		// final String blk = Util.toHexString(sta, 0, blkSize);
		// r.append("<br />S: ").append(blk);
		return r.toString();
	}
}
