package net.sourceforge.jwbf.actions.mw.meta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import net.sourceforge.jwbf.actions.Get;
import net.sourceforge.jwbf.actions.mw.HttpAction;
import net.sourceforge.jwbf.bots.MediaWikiBot;

import org.jdom.Element;

public class GetSiteinfo extends GetVersion {

	
	private Get msg;
	public GetSiteinfo() {
		
		

			try {
				msg = new Get("/api.php?action=query&meta=siteinfo" +
						"&siprop=" + URLEncoder.encode("general|namespaces", MediaWikiBot.CHARSET) +
						"&format=xml");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
	@Override
	public HttpAction getNextMessage() {
		return msg;
	}

	@SuppressWarnings("unchecked")
	protected void findContent(final Element root) {
		
		Iterator<Element> el = root.getChildren().iterator();
		while (el.hasNext()) {
			Element element = el.next();
			if (element.getQualifiedName().equalsIgnoreCase("general")) {

				site.setMainpage(element
						.getAttributeValue("mainpage"));
				site.setBase(element.getAttributeValue("base"));
				site.setSitename(element
						.getAttributeValue("sitename"));
				site.setGenerator(element
						.getAttributeValue("generator"));
				site.setCase(element.getAttributeValue("case"));
			} else if (element.getQualifiedName().equalsIgnoreCase("ns")) {
				Integer id = Integer.parseInt(element.getAttributeValue("id"));
				String name = element.getText();
				site.addNamespace(id, name);

			} else if (element.getQualifiedName().equalsIgnoreCase("iw")) {
				if (element.getAttribute("prefix") != null) {
					String prefix = element
							.getAttributeValue("prefix");
					String name = element.getAttributeValue("url");
					site.addInterwiki(prefix, name);
				}
			} else {
				findContent(element);
			}
		}
	}

	

	
	
}
