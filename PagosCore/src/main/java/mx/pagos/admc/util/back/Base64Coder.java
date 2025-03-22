// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, http://www.gnu.org/licenses/lgpl.html
//  AL, Apache License, http://www.apache.org/licenses
//  BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

package mx.pagos.admc.util.back;

/**
 * A Base64 encoder/decoder.
 * 
 * <p>
 * This class is used to encode and decode data in Base64 format as described in
 * RFC 1521.
 * 
 * <p>
 * Project home page: <a
 * href="http://www.source-code.biz/base64coder/java/">www.
 * source-code.biz/base64coder/java</a><br>
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
 * Multi-licensed: EPL / LGPL / AL / BSD.
 */
public class Base64Coder {

	// The line separator string of the operating system.
	private static final String systemLineSeparator = System.getProperty("line.separator");

	// Mapping table from 6-bit nibbles to Base64 characters.
	private static char[] map1 = new char[64];
	static {
		int i = 0;
		for (char c = 'A'; c <= 'Z'; c++)
			map1[i++] = c;
		for (char c = 'a'; c <= 'z'; c++)
			map1[i++] = c;
		for (char c = '0'; c <= '9'; c++)
			map1[i++] = c;
		map1[i++] = '+';
		map1[i++] = '/';
	}

	// Mapping table from Base64 characters to 6-bit nibbles.cd
	private static byte[] map2 = new byte[128];
	static {
		for (int i = 0; i < map2.length; i++)
			map2[i] = -1;
		for (int i = 0; i < 64; i++)
			map2[map1[i]] = (byte) i;
	}

	/**
	 * Encodes a string into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param s
	 *            A String to be encoded.
	 * @return A String containing the Base64 encoded data.
	 */
	public static String encodeString(String s) {
		return new String(encode(s.getBytes()));
	}

	/**
	 * Encodes a byte array into Base 64 format and breaks the output into lines
	 * of 76 characters. This method is compatible with
	 * <code>sun.misc.BASE64Encoder.encodeBuffer(byte[])</code>.
	 * 
	 * @param in
	 *            An array containing the data bytes to be encoded.
	 * @return A String containing the Base64 encoded data, broken into lines.
	 */
	public static String encodeLines(byte[] in) {
		return encodeLines(in, 0, in.length, 76, systemLineSeparator);
	}

	/**
	 * Encodes a byte array into Base 64 format and breaks the output into
	 * lines.
	 * 
	 * @param in
	 *            An array containing the data bytes to be encoded.
	 * @param iOff
	 *            Offset of the first byte in <code>in</code> to be processed.
	 * @param iLen
	 *            Number of bytes to be processed in <code>in</code>, starting
	 *            at <code>iOff</code>.
	 * @param lineLen
	 *            Line length for the output data. Should be a multiple of 4.
	 * @param lineSeparator
	 *            The line separator to be used to separate the output lines.
	 * @return A String containing the Base64 encoded data, broken into lines.
	 */
	public static String encodeLines(byte[] in, int iOff, int iLen,
			int lineLen, String lineSeparator) {
		int blockLen = (lineLen * 3) / 4;
		if (blockLen <= 0)
			throw new IllegalArgumentException();
		int lines = (iLen + blockLen - 1) / blockLen;
		int bufLen = ((iLen + 2) / 3) * 4 + lines * lineSeparator.length();
		StringBuilder buf = new StringBuilder(bufLen);
		int ip = 0;
		while (ip < iLen) {
			int l = Math.min(iLen - ip, blockLen);
			buf.append(encode(in, iOff + ip, l));
			buf.append(lineSeparator);
			ip += l;
		}
		return buf.toString();
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted in the output.
	 * 
	 * @param in
	 *            An array containing the data bytes to be encoded.
	 * @return A character array containing the Base64 encoded data.
	 */
	public static char[] encode(byte[] in) {
		return encode(in, 0, in.length);
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted in the output.
	 * 
	 * @param in
	 *            An array containing the data bytes to be encoded.
	 * @param iLen
	 *            Number of bytes to process in <code>in</code>.
	 * @return A character array containing the Base64 encoded data.
	 */
	public static char[] encode(byte[] in, int iLen) {
		return encode(in, 0, iLen);
	}

	/**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted in the output.
	 * 
	 * @param in
	 *            An array containing the data bytes to be encoded.
	 * @param iOff
	 *            Offset of the first byte in <code>in</code> to be processed.
	 * @param iLen
	 *            Number of bytes to process in <code>in</code>, starting at
	 *            <code>iOff</code>.
	 * @return A character array containing the Base64 encoded data.
	 */
	public static char[] encode(byte[] in, int iOff, int iLen) {
		int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
		int oLen = ((iLen + 2) / 3) * 4; // output length including padding
		char[] out = new char[oLen];
		int ip = iOff;
		int iEnd = iOff + iLen;
		int op = 0;
		while (ip < iEnd) {
			int i0 = in[ip++] & 0xff;
			int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
			int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
			int o0 = i0 >>> 2;
			int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
			int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
			int o3 = i2 & 0x3F;
			out[op++] = map1[o0];
			out[op++] = map1[o1];
			out[op] = op < oDataLen ? map1[o2] : '=';
			op++;
			out[op] = op < oDataLen ? map1[o3] : '=';
			op++;
		}
		return out;
	}

	/**
	 * Decodes a string from Base64 format. No blanks or line breaks are allowed
	 * within the Base64 encoded input data.
	 * 
	 * @param s
	 *            A Base64 String to be decoded.
	 * @return A String containing the decoded data.
	 * @throws IllegalArgumentException
	 *             If the input is not valid Base64 encoded data.
	 */
	public static String decodeString(String s) {
		return new String(decode(s));
	}

	/**
	 * Decodes a byte array from Base64 format and ignores line separators, tabs
	 * and blanks. CR, LF, Tab and Space characters are ignored in the input
	 * data. This method is compatible with
	 * <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.
	 * 
	 * @param s
	 *            A Base64 String to be decoded.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             If the input is not valid Base64 encoded data.
	 */
	public static byte[] decodeLines(String s) {
		char[] buf = new char[s.length()];
		int p = 0;
		for (int ip = 0; ip < s.length(); ip++) {
			char c = s.charAt(ip);
			if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
				buf[p++] = c;
		}
		return decode(buf, 0, p);
	}

	/**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded input data.
	 * 
	 * @param s
	 *            A Base64 String to be decoded.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             If the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(String s) {
		return decode(s.toCharArray());
	}

	/**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded input data.
	 * 
	 * @param in
	 *            A character array containing the Base64 encoded data.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             If the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in) {
		return decode(in, 0, in.length);
	}

	/**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded input data.
	 * 
	 * @param in
	 *            A character array containing the Base64 encoded data.
	 * @param iOff
	 *            Offset of the first character in <code>in</code> to be
	 *            processed.
	 * @param iLen
	 *            Number of characters to process in <code>in</code>, starting
	 *            at <code>iOff</code>.
	 * @return An array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 *             If the input is not valid Base64 encoded data.
	 */
	public static byte[] decode(char[] in, int iOff, int iLen) {
		if (iLen % 4 != 0)
			throw new IllegalArgumentException(
					"Length of Base64 encoded input string is not a multiple of 4.");
		while (iLen > 0 && in[iOff + iLen - 1] == '=')
			iLen--;
		int oLen = (iLen * 3) / 4;
		byte[] out = new byte[oLen];
		int ip = iOff;
		int iEnd = iOff + iLen;
		int op = 0;
		while (ip < iEnd) {
			int i0 = in[ip++];
			int i1 = in[ip++];
			int i2 = ip < iEnd ? in[ip++] : 'A';
			int i3 = ip < iEnd ? in[ip++] : 'A';
			if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			int b0 = map2[i0];
			int b1 = map2[i1];
			int b2 = map2[i2];
			int b3 = map2[i3];
			if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
				throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data.");
			int o0 = (b0 << 2) | (b1 >>> 4);
			int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
			int o2 = ((b2 & 3) << 6) | b3;
			out[op++] = (byte) o0;
			if (op < oLen)
				out[op++] = (byte) o1;
			if (op < oLen)
				out[op++] = (byte) o2;
		}
		return out;
	}

	// Dummy constructor.
	private Base64Coder() {
	}
	
	public static void main(String[] args) {
		
		
		//String s=Base64Coder.decodeString("77u/PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8Y2ZkaTpDb21wcm9iYW50ZSB4bWxuczp0ZmQ9Imh0dHA6Ly93d3cuc2F0LmdvYi5teC9UaW1icmVGaXNjYWxEaWdpdGFsIiB4bWxuczpjZmRpPSJodHRwOi8vd3d3LnNhdC5nb2IubXgvY2ZkLzMiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiIEx1Z2FyRXhwZWRpY2lvbj0iUXVlcmV0YXJvIFFybyIgdGlwb0RlQ29tcHJvYmFudGU9ImluZ3Jlc28iIG1ldG9kb0RlUGFnbz0iRUZFQ1RJVk8iIHRvdGFsPSIxNzIuMTIiIE1vbmVkYT0iTVhOIiBUaXBvQ2FtYmlvPSIxLjAiIHN1YlRvdGFsPSIxNDguMzgiIGNvbmRpY2lvbmVzRGVQYWdvPSJQQUdPIEVOIFVOQSBTT0xBIEVYSElCSUNJT04iIGNlcnRpZmljYWRvPSJNSUlFZERDQ0ExeWdBd0lCQWdJVU1qQXdNREV3TURBd01EQXhNREF3TURVNE5qY3dEUVlKS29aSWh2Y05BUUVGQlFBd2dnRnZNUmd3RmdZRFZRUUREQTlCTGtNdUlHUmxJSEJ5ZFdWaVlYTXhMekF0QmdOVkJBb01KbE5sY25acFkybHZJR1JsSUVGa2JXbHVhWE4wY21GamFjT3piaUJVY21saWRYUmhjbWxoTVRnd05nWURWUVFMREM5QlpHMXBibWx6ZEhKaFkybkRzMjRnWkdVZ1UyVm5kWEpwWkdGa0lHUmxJR3hoSUVsdVptOXliV0ZqYWNPemJqRXBNQ2NHQ1NxR1NJYjNEUUVKQVJZYVlYTnBjMjVsZEVCd2NuVmxZbUZ6TG5OaGRDNW5iMkl1YlhneEpqQWtCZ05WQkFrTUhVRjJMaUJJYVdSaGJHZHZJRGMzTENCRGIyd3VJRWQxWlhKeVpYSnZNUTR3REFZRFZRUVJEQVV3TmpNd01ERUxNQWtHQTFVRUJoTUNUVmd4R1RBWEJnTlZCQWdNRUVScGMzUnlhWFJ2SUVabFpHVnlZV3d4RWpBUUJnTlZCQWNNQ1VOdmVXOWhZOE9oYmpFVk1CTUdBMVVFTFJNTVUwRlVPVGN3TnpBeFRrNHpNVEl3TUFZSktvWklodmNOQVFrQ0RDTlNaWE53YjI1ellXSnNaVG9nU01PcFkzUnZjaUJQY201bGJHRnpJRUZ5WTJsbllUQWVGdzB4TWpBM01qY3hOekF5TURCYUZ3MHhOakEzTWpjeE56QXlNREJhTUlIYk1Ta3dKd1lEVlFRREV5QkJRME5GVFNCVFJWSldTVU5KVDFNZ1JVMVFVa1ZUUVZKSlFVeEZVeUJUUXpFcE1DY0dBMVVFS1JNZ1FVTkRSVTBnVTBWU1ZrbERTVTlUSUVWTlVGSkZVMEZTU1VGTVJWTWdVME14S1RBbkJnTlZCQW9USUVGRFEwVk5JRk5GVWxaSlEwbFBVeUJGVFZCU1JWTkJVa2xCVEVWVElGTkRNU1V3SXdZRFZRUXRFeHhCUVVFd01UQXhNREZCUVVFZ0x5QklSVWRVTnpZeE1EQXpORk15TVI0d0hBWURWUVFGRXhVZ0x5QklSVWRVTnpZeE1EQXpUVVJHVWs1T01Ea3hFVEFQQmdOVkJBc1RDRlZ1YVdSaFpDQXhNSUdmTUEwR0NTcUdTSWIzRFFFQkFRVUFBNEdOQURDQmlRS0JnUUMyVFRRU1BPTkJPVnhwWHY5d0xZbzhqZXpCcmIzNGkvdEx4OGpHZHR5eTI3QmNlc09hdjJjMU5TL0dkdjEwdTlTa1d0d2R5MzR1UkFWZTdIMGEzVk1STEhBa3ZwMnFNQ0hhWmM0VDhrNDdKdGI5d3JPRWgvWEZTOExnVDR5NU9RWW82Y2l2ZlhYZGx2eFdVL2dkTS9lNkkybGc2RkdvclA4SDRHUEFKL3FDTndJREFRQUJveDB3R3pBTUJnTlZIUk1CQWY4RUFqQUFNQXNHQTFVZER3UUVBd0lHd0RBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQVR4TWVjVHBNYmRoU0hvNktWVWc0UVZGNE9wMklCaGlNYU9ydHJYQmRKZ3pHb3RVRmNKZ2RCQ01qdFRaWFNscTFTNERHMWpyOHA0TnpRbHp4c2RUeGFCOG5TS0o0S0VNZ0lUN0U2MnhSVWoxNWpJNDlxRno3ZjJ1TXR0WkxOVGhpcHVuc04vTkYxWHR2RVNNVER3UUZ2YXMvVWdpZzZxd0VmU1pjME1EeE1wS0xFa0VlUG1Rd3RaRCt6WEZTTVZhNmhtT3U0TStGekdpUlhiajRZSlhuOU15amQ4eGJML2MrOVVJY3JZb1pza3hEdk14YzYvNk0zck5ORFkzT0ZoQksrVi9zUE16V1dHdDhTMXlqbXRQZlhnRnMxdDY1QVoyaGNUd1RBdUhyS3dEYXRKMVpQZmE0ODJaQlJPQUFYMXdhejdXd1hwMGdzbzdzRENtMi95VVZ3dz09IiBub0NlcnRpZmljYWRvPSIyMDAwMTAwMDAwMDEwMDAwNTg2NyIgZm9ybWFEZVBhZ289IkVGRUNUSVZPIiBzZWxsbz0iTHVzc0FGcDlLSmNjRVhsaTlzZ1V6eTJsUVgwaktualpkQ3dWNW0vSnI4a29NWWhVajgwMlliSXNEZXovQytkN3hYYjVzNlYyWnRpaEQrTHZwM0M0aGVBaHFjdUxZRTV0ZDlmOTZLRXZLT3plZURTT0lGd0VGYm43dmc2RDhFZ3BBbGNzSUdLYkRCSmlaT2sxK0MwVVB5eTlJZVByOFVYd1ZFZXJrK2I0ZjlVPSIgZmVjaGE9IjIwMTMtMTEtMTJUMTA6NDQ6NTYiIGZvbGlvPSIxIiB2ZXJzaW9uPSIzLjIiIHhzaTpzY2hlbWFMb2NhdGlvbj0iaHR0cDovL3d3dy5zYXQuZ29iLm14L2NmZC8zICBodHRwOi8vd3d3LnNhdC5nb2IubXgvc2l0aW9faW50ZXJuZXQvY2ZkLzMvY2ZkdjMyLnhzZCI+CiAgICA8Y2ZkaTpFbWlzb3Igbm9tYnJlPSJFTVBSRVNBIFBSVUVCQVMgU0EgREUgQ1YiIHJmYz0iQUFBMDEwMTAxQUFBIj4KICAgICAgICA8Y2ZkaTpEb21pY2lsaW9GaXNjYWwgY29kaWdvUG9zdGFsPSI3NjkwMCIgcGFpcz0iTUVYSUNPIiBlc3RhZG89IlFVRVJFVEFSTyIgbXVuaWNpcGlvPSJDT1JSRUdJRE9SQSIgY29sb25pYT0iRUwgUFVFQkxJVE8iIG5vRXh0ZXJpb3I9IjY3IiBjYWxsZT0iSEVST0lDTyBDT0xFR0lPIE1JTElUQVIiLz4KICAgICAgICA8Y2ZkaTpFeHBlZGlkb0VuIGNvZGlnb1Bvc3RhbD0iNzY5MDAiIHBhaXM9Ik1FWElDTyIgZXN0YWRvPSJRVUVSRVRBUk8iIG11bmljaXBpbz0iQ09SUkVHSURPUkEiIGNvbG9uaWE9IkVMIFBVRUJMSVRPIiBub0V4dGVyaW9yPSI2NyIgY2FsbGU9IkhFUk9JQ08gQ09MRUdJTyBNSUxJVEFSIi8+CiAgICAgICAgPGNmZGk6UmVnaW1lbkZpc2NhbCBSZWdpbWVuPSJSZWdpbWUgRGUgbGV5Ii8+CiAgICA8L2NmZGk6RW1pc29yPgogICAgPGNmZGk6UmVjZXB0b3Igbm9tYnJlPSJTSUVSUkEgRVNQQVJaQSBKT1NFRklOQSIgcmZjPSJTSUVKNzEwOTAzOTc3Ij4KICAgICAgICA8Y2ZkaTpEb21pY2lsaW8gY29kaWdvUG9zdGFsPSIzNzM1OCIgcGFpcz0iTUVYSUNPIiBlc3RhZG89IkdVQU5BSlVBVE8iIGNhbGxlPSJNSVJBRE9SIERFTCBWQUxMRSAyMDggQSIvPgogICAgPC9jZmRpOlJlY2VwdG9yPgogICAgPGNmZGk6Q29uY2VwdG9zPgogICAgICAgIDxjZmRpOkNvbmNlcHRvIGltcG9ydGU9IjE0OC4zOCIgdmFsb3JVbml0YXJpbz0iMTQ4LjM4IiBkZXNjcmlwY2lvbj0iTWFudGVuaW1pZW50byBkZSBTb2Z0d2FyZSBOT1JNQUwiIHVuaWRhZD0iU2VydmljaW8iIGNhbnRpZGFkPSIxIi8+CiAgICAgICAgPGNmZGk6Q29uY2VwdG8gaW1wb3J0ZT0iMC4wMCIgdmFsb3JVbml0YXJpbz0iMC4wMCIgZGVzY3JpcGNpb249IklOVEVSRVMgTU9SQVRPUklPIiB1bmlkYWQ9Ik5vIEFwbGljYSIgY2FudGlkYWQ9IjEiLz4KICAgIDwvY2ZkaTpDb25jZXB0b3M+CiAgICA8Y2ZkaTpJbXB1ZXN0b3MgdG90YWxJbXB1ZXN0b3NUcmFzbGFkYWRvcz0iMjMuNzQiPgogICAgICAgIDxjZmRpOlRyYXNsYWRvcz4KICAgICAgICAgICAgPGNmZGk6VHJhc2xhZG8gaW1wb3J0ZT0iMjMuNzQiIHRhc2E9IjE2LjAwIiBpbXB1ZXN0bz0iSVZBIi8+CiAgICAgICAgPC9jZmRpOlRyYXNsYWRvcz4KICAgIDwvY2ZkaTpJbXB1ZXN0b3M+CiAgICA8Y2ZkaTpDb21wbGVtZW50bz4KICAgICAgICA8dGZkOlRpbWJyZUZpc2NhbERpZ2l0YWwgRmVjaGFUaW1icmFkbz0iMjAxMy0xMS0xMlQxMjowMTowMyIgVVVJRD0iOWUxZDk4ZGUtOGE3ZS00ZGY1LWE5YmEtNWJjYjY4ZTk4NjdiIiBub0NlcnRpZmljYWRvU0FUPSIwMDAwMTAwMDAwMDMwMDE3MTI5MSIgc2VsbG9DRkQ9Ikx1c3NBRnA5S0pjY0VYbGk5c2dVenkybFFYMGpLbmpaZEN3VjVtL0pyOGtvTVloVWo4MDJZYklzRGV6L0MrZDd4WGI1czZWMlp0aWhEK0x2cDNDNGhlQWhxY3VMWUU1dGQ5Zjk2S0V2S096ZWVEU09JRndFRmJuN3ZnNkQ4RWdwQWxjc0lHS2JEQkppWk9rMStDMFVQeXk5SWVQcjhVWHdWRWVyaytiNGY5VT0iIHNlbGxvU0FUPSJDZTNTZGhidUk5a3JIa2xabTArMFFjR1JQQURjRGJxVUFsRkdkbEtNc0ZKNGk2bHkyNGdDTFBmOWgvdStDT0FzVjk1dGk0ZXdhc2Q1VFFBMCtyeVdnbGExSkhVbHU1dWZielNTYkF4Q25nSmo4cXdCUGlYK1ZlTXhOVHNGY3M4ckFEcldoQjNRSVd3L1lDOEova0hxeWFkWVFBN3pqeThtd2JTdExZL2wrcHc9IiB2ZXJzaW9uPSIxLjAiIHhzaTpzY2hlbWFMb2NhdGlvbj0iaHR0cDovL3d3dy5zYXQuZ29iLm14L1RpbWJyZUZpc2NhbERpZ2l0YWwgaHR0cDovL3d3dy5zYXQuZ29iLm14L1RpbWJyZUZpc2NhbERpZ2l0YWwvVGltYnJlRmlzY2FsRGlnaXRhbC54c2QiLz4KICAgIDwvY2ZkaTpDb21wbGVtZW50bz4KPC9jZmRpOkNvbXByb2JhbnRlPgo=");
		String s=Base64Coder.decodeString("U3NzazIwMjA=");
			System.out.println(s);
	}

} // end class Base64Coder