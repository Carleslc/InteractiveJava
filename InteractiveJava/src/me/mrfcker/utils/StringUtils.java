package me.mrfcker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class StringUtils {

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
			for (Entry<String, Object> var : variables.entrySet())
				s = s.replace(var.getKey(), var.getValue().toString());
		}
		return s;
	}

	public static String formatLiterals(String s) {
		if (s != null) {
			StringBuilder sb = new StringBuilder();
			boolean isLiteral = false;
			for (int i = 0; i < s.length(); ++i) {
				char at = s.charAt(i);
				if (isLiteral && Character.isSpaceChar(at))
					sb.append('�');
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
			args[i] = args[i].replace("�", " ");
		return args;
	}
	
	public static String source(String urlSite) throws IOException {
		StringBuilder result = new StringBuilder();

		URL url;
		URLConnection urlConn;

		url = new URL(urlSite);
		urlConn = url.openConnection();
		urlConn.addRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		
		Reader reader = new InputStreamReader(urlConn.getInputStream(),
				"utf-8");
		BufferedReader br = new BufferedReader(reader);
		
		String line;
		while ((line = br.readLine()) != null)
	        result.append(line + "\n");
		
	    br.close();

		return result.toString();
	}
	
    /**
     * Funci�n que elimina acentos y caracteres especiales de
     * una cadena de texto.
     * @param input
     * @return cadena de texto limpia de acentos y caracteres especiales en MAYUSCULAS
     */
    public static String withoutSpecials(String input) {
        // Descomposici�n can�nica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos �nicamente con los caracteres ASCII
        return Pattern.compile("\\P{ASCII}+").matcher(normalized).replaceAll("");
    }
}
