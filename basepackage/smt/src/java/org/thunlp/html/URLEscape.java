package org.thunlp.html;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLEscape {
	private static Pattern protocolPattern = Pattern.compile("([a-zA-Z]+:)");
	private static Pattern contentPattern = Pattern.compile("/([^/]*)");

	/**
	 * escaped an url using "UTF-8" encoding, and will not escape "?","=" and
	 * escaped characters
	 * 
	 * @param url
	 *            url to escape
	 * @param encoding
	 *            encoding
	 * @return the escaped url
	 * @throws UnsupportedEncodingException
	 */
	public static String escape(String url) throws UnsupportedEncodingException {
		return escape(url, "UTF-8");
	}

	/**
	 * escaped an url, and will not escape "?","=" and escaped characters
	 * 
	 * @param url
	 *            url to escape
	 * @param encoding
	 *            encoding
	 * @return the escaped url
	 * @throws UnsupportedEncodingException
	 */
	public static String escape(String url, String encoding) throws UnsupportedEncodingException {
		return escape(url, encoding, false);
	}

	/**
	 * escaped an url
	 * 
	 * @param url
	 *            url to escape
	 * @param encoding
	 *            encoding
	 * @param escapeAll
	 *            whether to escape all the characters except "a" through "z",
	 *            "A" through "Z", "0" through "9", ".", "-", "*", and "_".
	 * @return the escaped url
	 * @throws UnsupportedEncodingException
	 */
	public static String escape(String url, String encoding, boolean escapeAll) throws UnsupportedEncodingException {
		if (url == null)
			return url;
		Matcher protocolMatcher = protocolPattern.matcher(url);
		StringBuilder sb = new StringBuilder();
		if (protocolMatcher.find())
			sb.append(protocolMatcher.group());

		Matcher contentMatcher = contentPattern.matcher(url);
		while (contentMatcher.find()) {
			sb.append("/");
			String content = contentMatcher.group(1);
			if (escapeAll) {
				sb.append(java.net.URLEncoder.encode(content, encoding));
			} else {
				if (content.matches("((%[A-Z0-9][A-Z0-9])|[a-zA-Z0-9_*\\.\\+-\\?=])*"))
					sb.append(content);
				else {
					for (String part : split(content)) {
						if (part.equals("?") || part.equals("="))
							sb.append(part);
						else
							sb.append(java.net.URLEncoder.encode(part, encoding));
					}
				}
			}
		}
		return sb.toString();
	}

	private static List<String> split(String input) {
		List<String> result = new ArrayList<String>();
		String[] parts1 = input.split("\\?");
		for (int i = 0; i != parts1.length; i++) {
			String[] parts2 = parts1[i].split("=");
			for (int j = 0; j != parts2.length; j++) {
				// if (parts2[j].length() == 0)
				// continue;
				result.add(parts2[j]);
				if (j != parts2.length - 1)
					result.add("=");
			}
			if (i != parts1.length - 1)
				result.add("?");
		}
		return result;
	}

	public static void main(String[] argv) throws UnsupportedEncodingException {
		String url = "http://www.yeeyan.com/articles/view/?博贤-adfjalsdj_adsafsadf.ssssss?=    /46641/";
		// String url = "http://www.ecocn.org/wordpress/?p=106#more-106";
		System.out.println(escape(url, "UTF-8", true));
		System.out.println(escape(escape(url, "UTF-8", true)));
		System.out.println(escape(escape(url, "UTF-8", true), "UTF-8", true));
	}
}
