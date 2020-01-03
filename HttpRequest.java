import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 用于封装Http请求
 *
 */

public class HttpRequest {
	// 声明请求参数
	private String method;		// 请求方式
	private String uri;			// 请求资源路径
	private String protocol;	// 遵循的协议
	Map<String, String> dict = new HashMap<String, String>();

	public HttpRequest(InputStream in) {        // 初始化参数

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = reader.readLine();        // 获取请求行信息
			if (line != null && line.length() > 0) {
				// [GET./index.html,HTTP/1.1]
				System.out.println(line);
				String[] datas = line.split(" ");
				method = datas[0];        // 封装请求方式
				uri = datas[1];            // 封装请求路径
				protocol = datas[2];    // 封装协议名
			}
			if (method.equals("POST")) {
				String readLine;
				int contentLength = 0;
				while ((readLine = reader.readLine()) != null) {
					if ("".equals(readLine)) {
						break;
					} else if (readLine.contains("Content-Length")) {
						contentLength = Integer.parseInt(readLine.substring(readLine.indexOf("Content-Length") + 16));
					}
				}
				char[] buf;
				if (contentLength != 0) {
					buf = new char[contentLength];
					reader.read(buf, 0, contentLength);
					String[] params = new String(buf).split("&"); // �ָ��ֵ��
					for (String item : params) {
						String key = item.substring(0, item.indexOf("=")); // ��ȡ��ֵ��
						String val = item.substring(item.indexOf("=") + 1);
						dict.put(key, val);
					}
					System.out.println(dict.get("login"));
					System.out.println(dict.get("pass"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getMethod() {
		return method;
	}
	
	public String getUri() {
		return uri;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public Map<String, String> getDict() {
		return dict;
	}
}
