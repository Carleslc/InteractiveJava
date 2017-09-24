package me.mrfcker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

	public static final String LITERAL_REPLACEMENT = "$L$";
	
	private static final Pattern LITERAL_PATTERN = Pattern.compile("\".*\"");
	private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.######");
	
	public static String formatAsCommand(String s) {
		if (s != null) {
			s = s.trim();
			if (!s.isEmpty()) {
				String[] words = s.split("_");
				StringBuilder res = new StringBuilder();
				for (int i = 0; i < words.length; ++i) {
					res.append(capitalize(words[i]));
					if (i < words.length - 1)
						res.append("_");
				}
				return res.toString();
			}
		}
		return s;
	}

	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
	}

	public static String merge(Object[] args, int i, int length, String insert) {
		StringBuilder sb = new StringBuilder();
		for (; i < length; ++i)
			sb.append(args[i]).append(insert);
		return sb.toString();
	}

	public static String replaceVariables(String s, Map<String, Object> variables) {
		if (s != null) {
			// Remove literals
			List<String> literals = getLiterals(s);
			s = s.replaceAll(LITERAL_PATTERN.pattern(), "\\$!\\$");
			// Replace variables
			s = s.toLowerCase();
			for (Entry<String, Object> var : variables.entrySet())
				s = s.replace(var.getKey(), wrapDouble(var.getValue().toString()));
			// Add literals
			s = replaceLiterals(s, literals);
		}
		return s;
	}
	
	public static String wrapDouble(String s) {
		try {
			return DECIMAL_FORMAT.format(Double.parseDouble(s));
		} catch (NumberFormatException ignore) {
			return s;
		}
	}

	private static String replaceLiterals(String s, List<String> literals) {
		if (!literals.isEmpty()) {
			Matcher m = Pattern.compile("\\$!\\$").matcher(s);
			StringBuffer sb = new StringBuffer();
			int i = 0;
			while (m.find())
				m.appendReplacement(sb, literals.get(i++));
			s = m.appendTail(sb).toString();
		}
		return s;
	}
	
	public static List<String> getLiterals(String s) {
		List<String> literals = new ArrayList<>();
		Matcher m = LITERAL_PATTERN.matcher(s);
		while (m.find())
			literals.add(m.group(0));
		return literals;
	}

	public static String formatLiterals(String s) {
		if (s != null) {
			StringBuilder sb = new StringBuilder();
			boolean isLiteral = false;
			for (int i = 0; i < s.length(); ++i) {
				char at = s.charAt(i);
				if (isLiteral && Character.isSpaceChar(at))
					sb.append(LITERAL_REPLACEMENT);
				else if (at == '"')
					isLiteral = !isLiteral;
				else
					sb.append(at);
			}
			s = sb.toString();
		}
		return s;
	}

	public static String[] undoLiterals(String[] args) {
		for (int i = 0; i < args.length; ++i)
			args[i] = args[i].replace(LITERAL_REPLACEMENT, " ");
		return args;
	}
	
	public static String[] remove(String[] args, String substring) {
		List<String> argsList = Arrays.stream(args)
				.filter(arg -> !arg.equals(substring))
				.collect(Collectors.toList());
		return argsList.toArray(new String[argsList.size()]);
	}
	
	public static String concat(String[] args, int startIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < args.length; ++i) {
			sb.append(args[i]);
			if (i < args.length - 1)
				sb.append(" ");
		}
		return sb.toString();
	}
	
	public static String source(String urlSite) throws IOException {
		StringBuilder result = new StringBuilder();

		URL url;
		URLConnection urlConn;

		url = new URL(urlSite);
		urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		
		Reader reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
		BufferedReader br = new BufferedReader(reader);
		
		String line;
		while ((line = br.readLine()) != null)
	        result.append(line + "\n");
		
	    br.close();

		return result.toString();
	}
	
    /**
     * Funcion que elimina acentos y caracteres especiales de
     * una cadena de texto.
     * @param input
     * @return cadena de texto limpia de acentos y caracteres especiales en MAYUSCULAS
     */
    public static String withoutSpecials(String input) {
        // Descomposicion canonica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos \u00FAnicamente con los caracteres ASCII
        return Pattern.compile("\\P{ASCII}+").matcher(normalized).replaceAll("");
    }
}
