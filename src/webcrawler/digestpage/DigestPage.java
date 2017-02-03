package webcrawler.digestpage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gabe Sargeant on 22/01/17.
 */
public class DigestPage {


    public DigestPage() {

    }


    public Digest consume(Document doc, String url) {
        Digest digest = new Digest();
        

        Element body = doc.body();
        Elements elements = body.select("div, p, b");

        //title
        digest.setTitle(doc.title());
        //url
        digest.setURL(url);
        //date
        digest.setDate(getDate(doc.title()));
        //Country
        digest.setCountry(getCountry(elements));
        //member
        digest.setMember(getMember(elements));
        //place
        digest.setPlace(getPlace(elements));
        //decision_text
        digest.setDecisionText(getDecisionText(elements));
        //decision
        digest.setDecisionResult(getDecisionResult(elements));


        return digest;
    }

    private String getDecisionResult(Elements elements) {
        String text = "";
        for (Element l : elements) {
            if (l.text().contains("Decision") || (l.text().contains("DECISION:") && (!l.text().contains("PLACE")))) {
                if (l.text().toLowerCase().contains("remits")) {
                    text = "Remitted";
                } else {
                    text = "Affirmed";
                }
                break;
            }

        }
        return text;

    }

    private String getDecisionText(Elements elements) {
        String text = "";
        for (Element l : elements) {

            if (l.text().contains("Decision") || (l.text().contains("DECISION:") && (!l.text().contains("PLACE")))) {
                text = l.text();
                break;
            }
        }
        return text;
    }


    private String getMember(Elements elements) {

        String text = "";
        for (Element l : elements) {

            if (l.text().toLowerCase().contains("tribunal member:")) {
                //System.out.println(l.text());

                text = l.text().replace("Tribunal Member:", "");
                text = text.replace("TRIBUNAL MEMBER:", "");
                text.trim();

                break;


            }

            if (l.text().contains("Tribunal:")) {
                System.out.println(l.text());
                String tmp = l.text().replace("Tribunal:", "");
                text = tmp.replace("Member", "");
                text.trim();

                break;
            }

        }
        return text;

    }

    private Date getDate(String title) {
        Date date = null;

        int s = 0;
        int f = 0;
        s = title.lastIndexOf("(");
        f = title.lastIndexOf(")");
        if (s > 0 && f > 0) {
            Logger.debug("string date intex" + s + " " + f);
            String sdate = title.substring(s + 1, f);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMM yyyy");
            date = simpleDateFormat.parse(sdate, new ParsePosition(0));
            Logger.debug("this is the data as a date!" + date);
        }


        return date;
    }

//    private String getCountry(Elements elements) {
//        String text = "";
//        for (Element l : elements) {
//            //Country
//            if (l.text().toLowerCase().contains("")) {
//                //System.out.println(l.text());
//                text = l.text();
//                break;// jumps out of the loop
//            }
//
//        }
//
//        String country = "null";
//        String tmp;
//        text = text.toLowerCase();
//        for (String loc : cntry) {
//            tmp = loc.toLowerCase();
//            if (text.contains(tmp)) {
//                Logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ > "+ loc);
//                return loc;
//            }
//        }
//        Logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ > "+ country);
//        return country;
//    }

    private String getCountry(Elements elements) {
        String text = "";
        for (Element l : elements) {
            //Country
            text = checkCountry(l.text());
            if(text != null){
                Logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ > " + text);
                return text;
            }

        }

        return "null";
    }

    private String checkCountry(String text){
        String country = null;
        String tmp;
        text = text.toLowerCase();
        for (String loc : cntry) {
            tmp = loc.toLowerCase();
            if (text.contains(tmp)) {
                Logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ > " + loc);
                return loc;
            }
        }
        return country;
    }

    private String getPlace(Elements elements) {


        String text = "";
        for (Element l : elements) {
            //place
            if (l.text().toLowerCase().contains("place")) {
                text = l.text();

                break;
            }

        }


        String country = "null";
        String tmp;
        text = text.toLowerCase();
        for (String loc : places) {
            tmp = loc.toLowerCase();
            if (text.contains(tmp)) {
                return loc;
            }
        }
        return country;
    }


    private String[] places = {
            "Albury",
            "Wodonga",
            "Armidale",
            "Ballina",
            "Batemans Bay",
            "Bathurst",
            "Bowral",
            "Mittagong",
            "Broken Hill",
            "Camden Haven",
            "Central Coast",
            "Cessnock",
            "Coffs Harbour",
            "Dubbo",
            "Forster",
            "Tuncurry",
            "Goulburn",
            "Grafton",
            "Griffith",
            "Kurri Kurri",
            "Weston",
            "Lismore",
            "Lithgow",
            "Morisset",
            "Cooranbong",
            "Muswellbrook",
            "Nelson Bay",
            "Corlette",
            "Newcastle",
            "Maitland",
            "Nowra",
            "Bomaderry",
            "Orange",
            "Parkes",
            "Port Macquarie",
            "Singleton",
            "St Georges Basin",
            "Sanctuary Point",
            "Sydney",
            "Tamworth",
            "Taree",
            "Ulladulla",
            "Wagga Wagga",
            "Wollongong",
            "Bacchus Marsh",
            "Bairnsdale",
            "Ballarat",
            "Bendigo",
            "Colac",
            "Drysdale",
            "Clifton Springs",
            "Echuca",
            "Moama",
            "Geelong",
            "Gisborne",
            "Macedon",
            "Horsham",
            "Melbourne",
            "Melton",
            "Mildura",
            "Wentworth",
            "Moe",
            "Newborough",
            "Ocean Grove",
            "Point Lonsdale",
            "Sale",
            "Shepparton",
            "Mooroopna",
            "Torquay",
            "Traralgon",
            "Morwell",
            "Wangaratta",
            "Warragul",
            "Drouin",
            "Warrnambool",
            "Brisbane",
            "Bundaberg",
            "Cairns",
            "Emerald",
            "Gladstone",
            "Tannum Sands",
            "Gold Coast",
            "Tweed Heads",
            "Gympie",
            "Hervey Bay",
            "Highfields",
            "Mackay",
            "Maryborough",
            "Mount Isa",
            "Rockhampton",
            "Sunshine Coast",
            "Toowoomba",
            "Townsville",
            "Warwick",
            "Yeppoon",
            "Adelaide",
            "Mount Gambier",
            "Murray Bridge",
            "Port Augusta",
            "Port Lincoln",
            "Port Pirie",
            "Victor Harbor",
            "Goolwa",
            "Whyalla",
            "Albany",
            "Broome",
            "Bunbury",
            "Busselton",
            "Ellenbrook",
            "Geraldton",
            "Kalgoorlie",
            "Boulder",
            "Karratha",
            "Perth",
            "Port Hedland",
            "Burnie",
            "Wynyard",
            "Devonport",
            "Hobart",
            "Launceston",
            "Ulverstone",
            "Alice Springs",
            "Darwin",
            "Canberra",
            "Queanbeyan"};
    //have remove australi and canada
    private String[] cntry = {
            "Aruba",
            "Afghanistan",
            "Angola",
            "Anguilla",
            "Albania",
            "Andorra",
            "Netherlands Antilles",
            "United Arab Emirates",
            "Argentina",
            "Armenia",
            "American Samoa",
            "Antarctica",
            "French Southern territories",
            "Antigua and Barbuda",
            "Austria",
            "Azerbaijan",
            "Burundi",
            "Belgium",
            "Benin",
            "Burkina Faso",
            "Bangladesh",
            "Bulgaria",
            "Bahrain",
            "Bahamas",
            "Bosnia and Herzegovina",
            "Belarus",
            "Belize",
            "Bermuda",
            "Bolivia",
            "Brazil",
            "Barbados",
            "Brunei",
            "Bhutan",
            "Bouvet Island",
            "Botswana",
            "Central African Republic",

            "Cocos (Keeling) Islands",
            "Switzerland",
            "Chile",
            "China",
            "C�te d�Ivoire",
            "Cameroon",
            "Congo",
            "Congo",
            "Cook Islands",
            "Colombia",
            "Comoros",
            "Cape Verde",
            "Costa Rica",
            "Cuba",
            "Christmas Island",
            "Cayman Islands",
            "Cyprus",
            "Czech Republic",
            "Germany",
            "Djibouti",
            "Dominica",
            "Denmark",
            "Dominican Republic",
            "Algeria",
            "Ecuador",
            "Egypt",
            "Eritrea",
            "Western Sahara",
            "Spain",
            "Estonia",
            "Ethiopia",
            "Finland",
            "Fiji",
            "Falkland Islands",
            "France",
            "Faroe Islands",
            "Micronesia",
            "Gabon",
            "United Kingdom",
            "Georgia",
            "Ghana",
            "Gibraltar",
            "Guinea",
            "Guadeloupe",
            "Gambia",
            "Guinea-Bissau",
            "Equatorial Guinea",
            "Greece",
            "Grenada",
            "Greenland",
            "Guatemala",
            "French Guiana",
            "Guam",
            "Guyana",
            "Hong Kong",
            "Heard Island and McDonald Islands",
            "Honduras",
            "Croatia",
            "Haiti",
            "Hungary",
            "Indonesia",
            "India",
            "British Indian Ocean Territory",
            "Ireland",
            "Iran",
            "Iraq",
            "Iceland",
            "Israel",
            "Italy",
            "Jamaica",
            "Jordan",
            "Japan",
            "Kazakstan",
            "Kenya",
            "Kyrgyzstan",
            "Cambodia",
            "Kiribati",
            "Saint Kitts and Nevis",
            "South Korea",
            "Kuwait",
            "Laos",
            "Lebanon",
            "Liberia",
            "Libyan Arab Jamahiriya",
            "Saint Lucia",
            "Liechtenstein",
            "Sri Lanka",
            "Lesotho",
            "Lithuania",
            "Luxembourg",
            "Latvia",
            "Macao",
            "Morocco",
            "Monaco",
            "Moldova",
            "Madagascar",
            "Maldives",
            "Mexico",
            "Marshall Islands",
            "Macedonia",
            "Mali",
            "Malta",
            "Myanmar",
            "Mongolia",
            "Northern Mariana Islands",
            "Mozambique",
            "Mauritania",
            "Montserrat",
            "Martinique",
            "Mauritius",
            "Malawi",
            "Malaysia",
            "Mayotte",
            "Namibia",
            "New Caledonia",
            "Niger",
            "Norfolk Island",
            "Nigeria",
            "Nicaragua",
            "Niue",
            "Netherlands",
            "Norway",
            "Nepal",
            "Nauru",
            "New Zealand",
            "Oman",
            "Pakistan",
            "Panama",
            "Pitcairn",
            "Peru",
            "Philippines",
            "Palau",
            "Papua New Guinea",
            "Poland",
            "Puerto Rico",
            "North Korea",
            "Portugal",
            "Paraguay",
            "Palestine",
            "French Polynesia",
            "Qatar",
            "R�union",
            "Romania",
            "Rwanda",
            "Saudi Arabia",
            "Sudan",
            "Senegal",
            "Singapore",
            "South Georgia and the South Sandwich Islands",
            "Saint Helena",
            "Svalbard and Jan Mayen",
            "Solomon Islands",
            "Sierra Leone",
            "El Salvador",
            "San Marino",
            "Somalia",
            "Saint Pierre and Miquelon",
            "Sao Tome and Principe",
            "Suriname",
            "Slovakia",
            "Slovenia",
            "Sweden",
            "Swaziland",
            "Seychelles",
            "Syria",
            "Turks and Caicos Islands",
            "Chad",
            "Togo",
            "Thailand",
            "Tajikistan",
            "Tokelau",
            "Turkmenistan",
            "East Timor",
            "Tonga",
            "Trinidad and Tobago",
            "Tunisia",
            "Turkey",
            "Tuvalu",
            "Taiwan",
            "Tanzania",
            "Uganda",
            "Ukraine",
            "United States Minor Outlying Islands",
            "Uruguay",
            "United States",
            "Uzbekistan",
            "Holy See (Vatican City State)",
            "Saint Vincent and the Grenadines",
            "Venezuela",
            "Virgin Islands",
            "Virgin Islands",
            "Vietnam",
            "Vanuatu",
            "Wallis and Futuna",
            "Samoa",
            "Yemen",
            "Yugoslavia",
            "South Africa",
            "Zambia",
            "Zimbabwe",
            "Albania",
            "Algeria",
            "Andorra",
            "Angola",
            "Antigua & Deps",
            "Argentina",
            "Armenia",
            "Austria",
            "Azerbaijan",
            "Bahamas",
            "Bahrain",
            "Bangladesh",
            "Barbados",
            "Belarus",
            "Belgium",
            "Belize",
            "Benin",
            "Bhutan",
            "Bolivia",
            "Bosnia Herzegovina",
            "Botswana",
            "Brazil",
            "Brunei",
            "Bulgaria",
            "Burkina",
            "Burundi",
            "Cambodia",
            "Cameroon",

            "Cape Verde",
            "Central African Rep",
            "Chad",
            "Chile",
            "China",
            "Colombia",
            "Comoros",
            "Congo",
            "Congo {Democratic Rep}",
            "Costa Rica",
            "Croatia",
            "Cuba",
            "Cyprus",
            "Czech Republic",
            "Denmark",
            "Djibouti",
            "Dominica",
            "Dominican Republic",
            "East Timor",
            "Ecuador",
            "Egypt",
            "El Salvador",
            "Equatorial Guinea",
            "Eritrea",
            "Estonia",
            "Ethiopia",
            "Fiji",
            "Finland",
            "France",
            "Gabon",
            "Gambia",
            "Georgia",
            "Germany",
            "Ghana",
            "Greece",
            "Grenada",
            "Guatemala",
            "Guinea",
            "Guinea-Bissau",
            "Guyana",
            "Haiti",
            "Honduras",
            "Hungary",
            "Iceland",
            "India",
            "Indonesia",
            "Iran",
            "Iraq",
            "Ireland {Republic}",
            "Israel",
            "Italy",
            "Ivory Coast",
            "Jamaica",
            "Japan",
            "Jordan",
            "Kazakhstan",
            "Kenya",
            "Kiribati",
            "Korea North",
            "Korea South",
            "Kosovo",
            "Kuwait",
            "Kyrgyzstan",
            "Laos",
            "Latvia",
            "Lebanon",
            "Lesotho",
            "Liberia",
            "Libya",
            "Liechtenstein",
            "Lithuania",
            "Luxembourg",
            "Macedonia",
            "Madagascar",
            "Malawi",
            "Malaysia",
            "Maldives",
            "Mali",
            "Malta",
            "Marshall Islands",
            "Mauritania",
            "Mauritius",
            "Mexico",
            "Micronesia",
            "Moldova",
            "Monaco",
            "Mongolia",
            "Montenegro",
            "Morocco",
            "Mozambique",
            "Myanmar, {Burma}",
            "Namibia",
            "Nauru",
            "Nepal",
            "Netherlands",
            "New Zealand",
            "Nicaragua",
            "Niger",
            "Nigeria",
            "Norway",
            "Oman",
            "Pakistan",
            "Palau",
            "Panama",
            "Palestine",
            "Papua New Guinea",
            "Paraguay",
            "Peru",
            "Philippines",
            "Poland",
            "Portugal",
            "Qatar",
            "Romania",
            "Russia",
            "Rwanda",
            "St Kitts & Nevis",
            "St Lucia",
            "Saint Vincent & the Grenadines",
            "Samoa",
            "San Marino",
            "Sao Tome & Principe",
            "Saudi Arabia",
            "Senegal",
            "Serbia",
            "Seychelles",
            "Sierra Leone",
            "Singapore",
            "Slovakia",
            "Slovenia",
            "Solomon Islands",
            "Somalia",
            "South Africa",
            "South Sudan",
            "Spain",
            "Sri Lanka",
            "Sudan",
            "Suriname",
            "Swaziland",
            "Sweden",
            "Switzerland",
            "Syria",
            "Taiwan",
            "Tajikistan",
            "Tanzania",
            "Thailand",
            "Togo",
            "Tonga",
            "Trinidad & Tobago",
            "Tunisia",
            "Turkey",
            "Turkmenistan",
            "Tuvalu",
            "Uganda",
            "Ukraine",
            "United Arab Emirates",
            "United Kingdom",
            "United States",
            "Uruguay",
            "Uzbekistan",
            "Vanuatu",
            "Vatican City",
            "Venezuela",
            "Vietnam",
            "Yemen",
            "Viet Nam",
            "Kosovo",
            "Cote D'Ivoire",
            "Montenegro",
            "Burma",
            "Timor Leste",
            "East Timor ",
            "Occupied Territories",
            "China ",
            "Bagladesh",
            "Russia",
            "Serbia",
            "Thailand",
            "Indonesia",
            "Serbia",
            "Yugoslavia",
            "BurmaBurmaBurma",
            "Bangladesh",
            "NepalNepalNepal",
            "Occupied Territories",
            "Afghanisan",
            "Russia",
            "Stateless",
            "Bosnia",
            "AfghanistanAfghanistanAfghanistan",
            "Iraq/Iran",
            "MalaysiaMalaysiaMalaysia",
            "ColombiaColombiaColombia",
            "AfghanistanAfghanistanAfghanistan",
            "IndonesiaIndonesiaIndonesia",
            "Sri Lanka",
            "Burma",
            "Fiji ",
            "Serbia",
            "Fiji",
            "Occupied Territories",
            "Serbia",
            "Bosnia",
            "West Bank",
            "Libya",
            "RussiaCountry:Country",
            "China",
            "Kazakhstan"};

}


