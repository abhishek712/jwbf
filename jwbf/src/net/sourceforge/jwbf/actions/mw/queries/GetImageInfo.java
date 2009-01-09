package net.sourceforge.jwbf.actions.mw.queries;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import net.sourceforge.jwbf.actions.Get;
import net.sourceforge.jwbf.actions.mw.HttpAction;
import net.sourceforge.jwbf.actions.mw.util.ActionException;
import net.sourceforge.jwbf.actions.mw.util.MWAction;
import net.sourceforge.jwbf.actions.mw.util.ProcessException;
import net.sourceforge.jwbf.actions.mw.util.VersionException;
import net.sourceforge.jwbf.bots.MediaWikiBot;
import net.sourceforge.jwbf.bots.MediaWikiBotImpl;
import net.sourceforge.jwbf.contentRep.mw.Version;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * 
 * @author Thomas Stock
 * 
 * @supportedBy MediaWikiAPI 1.11, 1.12, 1.13
 *
 */

public class GetImageInfo extends MWAction {

	private String urlOfImage  = "";
	private Get msg;

	GetImageInfo(String name, Version v) throws VersionException {
		
		switch (v) {
		case MW1_09:
		case MW1_10:
			throw new VersionException("Not supportet by this version of MW");
			
		default:
			try {
				msg = new Get("/api.php?action=query&titles=Image:"
						+ URLEncoder.encode(name, MediaWikiBot.CHARSET)
						+ "&prop=imageinfo"
						+ "&iiprop=url"
						+ "&format=xml");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			break;
		}
		
	}

	


	public String getUrlAsString() {
		return urlOfImage  ;
	}

	@Override
	public String processAllReturningText(String s) throws ProcessException {
		findUrlOfImage(s);
		return "";
	}
	

	@SuppressWarnings("unchecked")
	private void findContent(final Element root) {

		Iterator<Element> el = root.getChildren().iterator();
		while (el.hasNext()) {
			Element element = el.next();
			if (element.getQualifiedName().equalsIgnoreCase("ii")) {
				urlOfImage = element.getAttributeValue("url");
				return;
			} else {
				findContent(element);
			}

		}
	}

	private void findUrlOfImage(String s) {
		SAXBuilder builder = new SAXBuilder();
		Element root = null;
		try {
			Reader i = new StringReader(s);
			Document doc = builder.build(new InputSource(i));

			root = doc.getRootElement();

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		findContent(root);
		
	}

	/**
	 * 
	 * Get a relativ url to an image.
	 * @param bot a
	 * @param imagename name of like "Test.gif"
	 * @return position like "http://server.tld/path/to/Test.gif"
	 * @throws ActionException on problems with http, cookies and io
	 * @throws ProcessException on inner problems, like unsopportet version
	 * @supportedBy MediaWikiAPI 1.11, 1.12, 1.13, 1.14
	 */
	public static String get(MediaWikiBotImpl bot, String imagename) throws ActionException, ProcessException {
		GetImageInfo a = new GetImageInfo(imagename, bot.getVersion());
		bot.performAction(a);
		String out = a.getUrlAsString();
		try {
			new URL(out);
		} catch (MalformedURLException e) {
			out = bot.getHostUrl() + out;
		}
		
		return out;
	}

	public HttpAction getNextMessage() {
		return msg;
	}
}
