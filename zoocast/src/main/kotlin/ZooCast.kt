import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import java.util.stream.Collectors


fun main(){
    println("ZoooCast start")
    val startedTime = Instant.now()

    val today = LocalDate.now()
    val lastMonth = today.minusMonths(1)

    println("today is $today")


    val days = lastMonth.datesUntil(today)
        .collect(Collectors.toList())
            //add today
        .toMutableList().apply {
            add(today)
        }
            //reverse, we want the latest episodes first
        .reversed()
            //filter weekends, there are no episodes on weekends
        .filter {
            it.dayOfWeek != DayOfWeek.SATURDAY || it.dayOfWeek == DayOfWeek.SUNDAY;
        }

    val episodes = days
        .subList(0, 5)
        .map {
        getEpisode(it.atTime(16, 0,0))
    }

    //TODO - we could verify these urls are valid, but will not handle it for now, leaving more freedom to the podcasts apps
    //worst case you will see invalid episodes for days that the zoo was not live (vacation or other stuff going on)

    //we can locally see some logs during development
//    episodes.forEach {
//        //it.log()
//        println("${it.date} - ${it.mp3}")
//        println("Exists: ${   urlExists(it.mp3)}")
//    }

    //les create the real feed now
    saveFeed(episodes)

    val endTime = Instant.now()
    val exectutionTime = endTime.toEpochMilli() - startedTime.toEpochMilli()
    println("ZoooCast end in $exectutionTime milliseconds")
}

private fun getEpisode(date: LocalDateTime) : Episode {
    println("getEpisode: $date")
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)

    val episodeDate  = date.atZone(ZoneId.of("Europe/Paris")).format(formatter)

    val giornoShort = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ITALIAN)
    val meseFull = date.month.getDisplayName(TextStyle.FULL, Locale.ITALIAN)
    val giornoFull = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ITALIAN)

    val streamingUrl  =  "https://podcast.mediaset.net/repliche//${date.year}/${date.monthValue}/${date.dayOfMonth}/${giornoShort}_${date.dayOfMonth.withZero()}${date.monthValue.withZero()}${date.year}_zoo.mp3";
    return Episode(
         title = "Puntata di $giornoFull ${date.dayOfMonth} $meseFull ${date.year} ",
         summary = "Puntata intera di $giornoFull ${date.dayOfMonth} ",
         description = "Puntata di $giornoFull ${date.dayOfMonth} $meseFull ${date.year}",
         mp3 = streamingUrl,
         date = episodeDate,
         id = "${date.month}_${date.dayOfMonth}_${date.year}"
    )

}
fun Int.withZero() : String {
    return if(this > 9){
        this.toString()
    }else{
        "0$this"
    }
}

private fun saveFeed(puntate: List<Episode>){
    val fileName = "zoo.xml"
    val feedFile = File(fileName)
    feedFile.printWriter().use { out ->
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        out.println("<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\">")
        out.println("<channel>")
        out.println("<title>Lo Zoo di 105 - Unofficial</title>")
        out.println("<link>http://zoo105.atwebpages.com/info.html</link>")
        out.println("<language>it</language>")
        out.println("<itunes:subtitle>Le puntate intere dello zoo di 105</itunes:subtitle>")
        out.println("<itunes:author>misterKola</itunes:author>")
        out.println("<itunes:summary>Podcast dello zo di 105 - Unofficial</itunes:summary>")
        out.println("<description>Podcast dello zo di 105 - con le punate inetre.</description>")
        out.println("<itunes:owner>")
        out.println("    <itunes:name>Marco Mallozi</itunes:name>")
        out.println("    <itunes:email>ihrkola@gmail.com</itunes:email>")
        out.println("</itunes:owner>")
        out.println("<itunes:explicit>no</itunes:explicit>")
        out.println("<itunes:image href=\"https://raw.githubusercontent.com/eltonkola/podcast_zoo_105/master/zoo_cover.jpg\" />")
        out.println("<itunes:category text=\"Comedy\"></itunes:category>")

        puntate.forEach {
            out.println("<item>")
            out.println("    <title>${it.title}</title>")
            out.println("    <itunes:summary>${it.summary}</itunes:summary>")
            out.println("    <description>${it.description}</description>")
            out.println("    <link>http://zoo105.atwebpages.com/index.html</link>")
            out.println("    <enclosure url=\"${it.mp3}\" type=\"audio/mpeg\" length=\"1024\"></enclosure>")
            out.println("    <pubDate>${it.date}</pubDate>")
            out.println("    <itunes:author>Zoo di 105</itunes:author>")
            out.println("    <itunes:duration>01:30:00</itunes:duration>")
            out.println("    <itunes:explicit>yes</itunes:explicit>")
            out.println("    <guid>${it.id}</guid>")
            out.println("</item>")
        }

        out.println("</channel>\n")
        out.println("</rss>")

    }
}

data class Episode(
    val title : String,
    val summary : String,
    val description : String,
    val mp3 : String,
    val date : String,
    val id: String
)

fun Episode.log(){
    println(">>>>>>>>>>>>")
    println("title: $title")
    println("summary: $summary")
    println("description: $description")
    println("mp3: $mp3")
    println("date: $date")
    println("id: $id")
}

fun urlExists(url: String) : Boolean {
    val u = URL(url)
    val huc: HttpURLConnection = u.openConnection() as HttpURLConnection
    huc.requestMethod = "HEAD"
    HttpURLConnection.setFollowRedirects(false)
    huc.connect()
    val responseCode: Int = huc.responseCode
    return responseCode == HttpURLConnection.HTTP_OK;
}
