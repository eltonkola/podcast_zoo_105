<?php
header("Content-type: text/xml");

setlocale(LC_TIME, 'ita');

function dita($datax){
  $giorni = array('dom','lun','mar','mer','gio','ven','sab');
  $giorniFull = array('Domenica','Lunedi','Martedi','Mercoledi','Giovedi','Venerdi','Sabato');
  $mesiFull = array("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre","Novembre", "Dicembre");
  
  list($sett,$giorno,$mese,$anno) = explode('-', date('w-d-n-Y',$datax));
  
  $mese2 = $mese;
  $giorno2 = $giorno;

  if( strlen($mese) == 1 ) {
    $mese2 = "0". $mese2;
  }

  
  if( strlen($giorno) == 2 ) {
    $giorno = str_replace("0", "", $giorno);
  }


  if($sett > 0 && $sett< 6){
   $urlxx =  "https://podcast.mediaset.net/repliche//". $anno ."/". $mese ."/". $giorno ."/". $giorni[$sett] ."_". $giorno2 ."". $mese2 . "". $anno."_zoo.mp3";
  

   $pubDate= date("D, d M Y H:i:s T", $datax);

   print("<item>\n");
   print("    <title>Puntata di ". $giorniFull[$sett] ." " . $giorno . " " . $mesiFull[$mese] . " " . $anno . " </title>\n");
   print("    <itunes:summary>Puntata intera di ". $giorniFull[$sett] ."</itunes:summary>\n");
   print("    <description>Puntata intera di ". $giorniFull[$sett] ." " . $giorno . " " . $mesiFull[$mese] . " " . $anno . "</description>\n");
   print("    <link>http://zoo105.atwebpages.com/index.html</link>\n");
   print("    <enclosure url=\"". $urlxx ."\" type=\"audio/mpeg\" length=\"1024\"></enclosure>\n");
   print("    <pubDate>". $pubDate ."</pubDate>\n");
   print("    <itunes:author>Zoo di 105</itunes:author>\n");
   print("    <itunes:duration>01:30:00</itunes:duration>\n");
   print("    <itunes:explicit>yes</itunes:explicit>\n");
   print("    <guid>". $giorni[$sett] ."_". $giorno ."". $mese2 . "". $anno."</guid>\n");
   print("</item>\n");

  }
  
}

function reformatDate($date, $from_format = 'w-d-n-Y', $to_format = 'D, d M Y H:i:s T') {
    $date_aux = date_create_from_format($from_format, $date);
    return date_format($date_aux,$to_format);
}

print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
print("<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\" version=\"2.0\">\n");
print("<channel>\n");
print("<title>Lo Zoo di 105 - Unofficial</title>\n");
print("<link>http://zoo105.atwebpages.com/info.html</link>\n");
print("<language>it</language>\n");
print("<itunes:subtitle>Le puntate intere dello zoo di 105</itunes:subtitle>\n");
print("<itunes:author>Author Name</itunes:author>\n");
print("<itunes:summary>Description of podcast.</itunes:summary>\n");
print("<description>Description of podcast.</description>\n");
print("<itunes:owner>\n");
print("    <itunes:name>Marco Mallozi</itunes:name>\n");
print("    <itunes:email>ihrkola@gmail.com</itunes:email>\n");
print("</itunes:owner>\n");
print("<itunes:explicit>no</itunes:explicit>\n");
print("<itunes:image href=\"https://raw.githubusercontent.com/eltonkola/podcast_zoo_105/master/zoo_cover.jpg\" />\n");
print("<itunes:category text=\"Comedy\"></itunes:category>\n");

$limit = isset($_GET['limit']) ? $_GET['limit'] : 1;
for($i=0; $i<=$limit; $i++){
        dita(date(strtotime("-$i day")));
}

print("</channel>\n");
print("</rss>");
?>