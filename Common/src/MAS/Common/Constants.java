package MAS.Common;

public class Constants {

    public static final String WEB_ROOT = "http://localhost:8080/InternalWeb_war_exploded/";
    public static final String WEB_ROOT_EXT = "http://localhost:8080/ExternalWeb_war_exploded/";
    public static final int INACTIVITY_TIMEOUT = 30;
    public static final String[] NATIONALITIES = {"Afghan", "Albanian", "Algerian", "American", "Andorran", "Angolan", "Antiguans", "Argentinean", "Armenian", "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian", "Barbudans", "Batswana", "Belarusian", "Belgian", "Belizean", "Beninese", "Bhutanese", "Bolivian", "Bosnian", "Brazilian", "British", "Bruneian", "Bulgarian", "Burkinabe", "Burmese", "Burundian", "Cambodian", "Cameroonian", "Canadian", "Cape Verdean", "Central African", "Chadian", "Chilean", "Chinese", "Colombian", "Comoran", "Congolese", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech", "Danish", "Djibouti", "Dominican", "Dutch", "East Timorese", "Ecuadorean", "Egyptian", "Emirian", "Equatorial Guinean", "Eritrean", "Estonian", "Ethiopian", "Fijian", "Filipino", "Finnish", "French", "Gabonese", "Gambian", "Georgian", "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan", "Guinea-Bissauan", "Guinean", "Guyanese", "Haitian", "Herzegovinian", "Honduran", "Hungarian", "I-Kiribati", "Icelander", "Indian", "Indonesian", "Iranian", "Iraqi", "Irish", "Israeli", "Italian", "Ivorian", "Jamaican", "Japanese", "Jordanian", "Kazakhstani", "Kenyan", "Kittian and Nevisian", "Kuwaiti", "Kyrgyz", "Laotian", "Latvian", "Lebanese", "Liberian", "Libyan", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian", "Malaysian", "Maldivan", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican", "Micronesian", "Moldovan", "Monacan", "Mongolian", "Moroccan", "Mosotho", "Motswana", "Mozambican", "Namibian", "Nauruan", "Nepalese", "New Zealander", "Nicaraguan", "Nigerian", "Nigerien", "North Korean", "Northern Irish", "Norwegian", "Omani", "Pakistani", "Palauan", "Panamanian", "Papua New Guinean", "Paraguayan", "Peruvian", "Polish", "Portuguese", "Qatari", "Romanian", "Russian", "Rwandan", "Saint Lucian", "Salvadoran", "Samoan", "San Marinese", "Sao Tomean", "Saudi", "Scottish", "Senegalese", "Serbian", "Seychellois", "Sierra Leonean", "Singaporean", "Slovakian", "Slovenian", "Solomon Islander", "Somali", "South African", "South Korean", "Spanish", "Sri Lankan", "Sudanese", "Surinamer", "Swazi", "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajik", "Tanzanian", "Thai", "Togolese", "Tongan", "Trinidadian or Tobagonian", "Tunisian", "Turkish", "Tuvaluan", "Ugandan", "Ukrainian", "Uruguayan", "Uzbekistani", "Venezuelan", "Vietnamese", "Welsh", "Yemenite", "Zambian", "Zimbabwean"};
    public static final String[] COUNTRIES = {"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua And Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan Republic", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bonaire, Sint Eustatius and Saba", "Bosnia Herzegovina", "Botswana", "Brazil", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chagos Archipelago", "Chile", "Christmas Island", "Cocos Island", "Colombia", "Commonwealth of Dominica", "Comoros", "Cook Islands", "Costa Rica", "Cote D'Ivoire", "Croatia", "Cuba", "Curacao", "Cyprus", "Czech Republic", "Democratic Republic of Congo", "Denmark", "Djibouti", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong SAR", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kingdom Of Lesotho", "Kiribati", "Korea (North)", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Marianas Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "People's Republic Of China", "Peru", "Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Republic Of Seychelles", "Republic of San Marino", "Republic of the Congo", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Samoa", "Sao Tome ", "Saudi Arabia", "Senegal", "Serbia", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "St Pierre and Miquelon", "St Vincent And The Grenadines", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajlkistan", "Tanzania", "Thailand", "Timor Leste", "Togo", "Tokelau", "Tonga", "Trinidad", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caios Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States Of America", "Unites States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State (Holy See)", "Venezuela", "Vietnam", "Virgin Islands (GB)", "Virgin Islands(U.S.)", "Walls Islands", "Yemen", "Zambia", "Zimbabwe"};

    public static final int FFP_TIER_BLUE = 1;
    public static final int FFP_TIER_SILVER = 2;
    public static final int FFP_TIER_GOLD = 3;
    public static final String FFP_TIER_BLUE_LABEL = "Elite Blue";
    public static final String FFP_TIER_SILVER_LABEL = "Elite Silver";
    public static final String FFP_TIER_GOLD_LABEL = "Elite Gold";

    public static final String[] COSTS = {"Consumables Per Flight", "Aircraft One-Time Cost",
            "Cost Per Maintenance", "Market Fuel Cost", "Annual Cost"};

    public static final int COST_PER_FLIGHT = 0;
    public static final int COST_PER_AIRCRAFT = 1;
    public static final int COST_PER_MAINTENANCE = 2;
    public static final int COST_FUEL = 3;
    public static final int COST_ANNUAL = 4;

    //public static final String FARE_NORMAL;

    public static final String AVERAGE_PERSON_WEIGHT = "AVERAGE_PERSON_WEIGHT";
    public static final String AVERAGE_BAGGAGE_WEIGHT = "AVERAGE_BAGGAGE_WEIGHT";
    public static final String FUEL_WEIGHT = "FUEL_WEIGHT";
    public static final String CABIN_CREW_SALARY = "CABIN_CREW_SALARY";
    public static final String COCKPIT_CREW_SALARY = "COCKPIT_CREW_SALARY";
    public static final String FLIGHTS_PER_YEAR = "FLIGHTS_PER_YEAR";
    public static final String MAINTENANCE_PER_YEAR = "MAINTENANCE_PER_YEAR";
    public static final String AIRCRAFT_EXPECTED_LIFE = "AIRCRAFT_EXPECTED_LIFE";
    public static final String DEMAND_STDEV = "DEMAND_STDEV";

    public static final String[] BOOKING_CLASS_NAMES = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static final double[] TRAVEL_CLASS_PRICE_MULTIPLIER = {6, 3, 1.5, 1};
    public static final double PROFIT_MARGIN = 1.4;
}
