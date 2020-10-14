package fitnesse.wikitext.parser3;

import fitnesse.html.HtmlTag;
import fitnesse.util.Clock;
import fitnesse.wiki.WikiPageProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class LastModified {
  static String translate(Symbol symbol, Translator translator) { //todo: dry with v2
    String user = translator.getExternal().getProperty(WikiPageProperty.LAST_MODIFYING_USER);
    String date = translator.getExternal().getProperty(WikiPageProperty.LAST_MODIFIED);
    return HtmlTag.name("span").attribute("class", "meta").body(
      "Last modified " +
        (!user.isEmpty() ? "by " + user : "anonymously") +
        " on " + formatDate(date)).html();
  }

  private static String formatDate(String dateString) {
    Date date;
    if (dateString.isEmpty()) date = Clock.currentDate();
    else {
      try {
        date = WikiPageProperty.getTimeFormat().parse(dateString);
      }
      catch (ParseException e) {
        return dateString;
      }
    }
    return new SimpleDateFormat("MMM dd, yyyy").format(date) + " at " + new SimpleDateFormat("hh:mm:ss a").format(date);
  }
}
