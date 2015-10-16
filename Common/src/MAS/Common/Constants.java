package MAS.Common;

public class Constants {

    public static final String WEB_ROOT = "http://localhost:8080/InternalWeb_war_exploded/";
    public static final String WEB_ROOT_EXT = "http://localhost:8080/ExternalWeb_war_exploded/";
    public static final int INACTIVITY_TIMEOUT = 30;

    public static final String[] NATIONALITIES = {"Afghan", "Albanian", "Algerian", "American", "Andorran", "Angolan", "Antiguans", "Argentinean", "Armenian", "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian", "Barbudans", "Batswana", "Belarusian", "Belgian", "Belizean", "Beninese", "Bhutanese", "Bolivian", "Bosnian", "Brazilian", "British", "Bruneian", "Bulgarian", "Burkinabe", "Burmese", "Burundian", "Cambodian", "Cameroonian", "Canadian", "Cape Verdean", "Central African", "Chadian", "Chilean", "Chinese", "Colombian", "Comoran", "Congolese", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech", "Danish", "Djibouti", "Dominican", "Dutch", "East Timorese", "Ecuadorean", "Egyptian", "Emirian", "Equatorial Guinean", "Eritrean", "Estonian", "Ethiopian", "Fijian", "Filipino", "Finnish", "French", "Gabonese", "Gambian", "Georgian", "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan", "Guinea-Bissauan", "Guinean", "Guyanese", "Haitian", "Herzegovinian", "Honduran", "Hungarian", "I-Kiribati", "Icelander", "Indian", "Indonesian", "Iranian", "Iraqi", "Irish", "Israeli", "Italian", "Ivorian", "Jamaican", "Japanese", "Jordanian", "Kazakhstani", "Kenyan", "Kittian and Nevisian", "Kuwaiti", "Kyrgyz", "Laotian", "Latvian", "Lebanese", "Liberian", "Libyan", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian", "Malaysian", "Maldivan", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican", "Micronesian", "Moldovan", "Monacan", "Mongolian", "Moroccan", "Mosotho", "Motswana", "Mozambican", "Namibian", "Nauruan", "Nepalese", "New Zealander", "Nicaraguan", "Nigerian", "Nigerien", "North Korean", "Northern Irish", "Norwegian", "Omani", "Pakistani", "Palauan", "Panamanian", "Papua New Guinean", "Paraguayan", "Peruvian", "Polish", "Portuguese", "Qatari", "Romanian", "Russian", "Rwandan", "Saint Lucian", "Salvadoran", "Samoan", "San Marinese", "Sao Tomean", "Saudi", "Scottish", "Senegalese", "Serbian", "Seychellois", "Sierra Leonean", "Singaporean", "Slovakian", "Slovenian", "Solomon Islander", "Somali", "South African", "South Korean", "Spanish", "Sri Lankan", "Sudanese", "Surinamer", "Swazi", "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajik", "Tanzanian", "Thai", "Togolese", "Tongan", "Trinidadian or Tobagonian", "Tunisian", "Turkish", "Tuvaluan", "Ugandan", "Ukrainian", "Uruguayan", "Uzbekistani", "Venezuelan", "Vietnamese", "Welsh", "Yemenite", "Zambian", "Zimbabwean"};

    public static final String[] COUNTRY_NAMES = {"Afghanistan", "Aland Islands", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bonaire, Saint Eustatius and Saba ", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Curacao", "Cyprus", "Czech Republic", "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Reunion", "Romania", "Russia", "Rwanda", "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Serbia and Montenegro", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "U.S. Virgin Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara", "Yemen", "Zambia", "Zimbabwe"};
    public static final String[] COUNTRY_CODES = {"AFG", "ALA", "ALB", "DZA", "ASM", "AND", "AGO", "AIA", "ATA", "ATG", "ARG", "ARM", "ABW", "AUS", "AUT", "AZE", "BHS", "BHR", "BGD", "BRB", "BLR", "BEL", "BLZ", "BEN", "BMU", "BTN", "BOL", "BES", "BIH", "BWA", "BVT", "BRA", "IOT", "VGB", "BRN", "BGR", "BFA", "BDI", "KHM", "CMR", "CAN", "CPV", "CYM", "CAF", "TCD", "CHL", "CHN", "CXR", "CCK", "COL", "COM", "COK", "CRI", "HRV", "CUB", "CUW", "CYP", "CZE", "COD", "DNK", "DJI", "DMA", "DOM", "TLS", "ECU", "EGY", "SLV", "GNQ", "ERI", "EST", "ETH", "FLK", "FRO", "FJI", "FIN", "FRA", "GUF", "PYF", "ATF", "GAB", "GMB", "GEO", "DEU", "GHA", "GIB", "GRC", "GRL", "GRD", "GLP", "GUM", "GTM", "GGY", "GIN", "GNB", "GUY", "HTI", "HMD", "HND", "HKG", "HUN", "ISL", "IND", "IDN", "IRN", "IRQ", "IRL", "IMN", "ISR", "ITA", "CIV", "JAM", "JPN", "JEY", "JOR", "KAZ", "KEN", "KIR", "XKX", "KWT", "KGZ", "LAO", "LVA", "LBN", "LSO", "LBR", "LBY", "LIE", "LTU", "LUX", "MAC", "MKD", "MDG", "MWI", "MYS", "MDV", "MLI", "MLT", "MHL", "MTQ", "MRT", "MUS", "MYT", "MEX", "FSM", "MDA", "MCO", "MNG", "MNE", "MSR", "MAR", "MOZ", "MMR", "NAM", "NRU", "NPL", "NLD", "ANT", "NCL", "NZL", "NIC", "NER", "NGA", "NIU", "NFK", "PRK", "MNP", "NOR", "OMN", "PAK", "PLW", "PSE", "PAN", "PNG", "PRY", "PER", "PHL", "PCN", "POL", "PRT", "PRI", "QAT", "COG", "REU", "ROU", "RUS", "RWA", "BLM", "SHN", "KNA", "LCA", "MAF", "SPM", "VCT", "WSM", "SMR", "STP", "SAU", "SEN", "SRB", "SCG", "SYC", "SLE", "SGP", "SXM", "SVK", "SVN", "SLB", "SOM", "ZAF", "SGS", "KOR", "SSD", "ESP", "LKA", "SDN", "SUR", "SJM", "SWZ", "SWE", "CHE", "SYR", "TWN", "TJK", "TZA", "THA", "TGO", "TKL", "TON", "TTO", "TUN", "TUR", "TKM", "TCA", "TUV", "VIR", "UGA", "UKR", "ARE", "GBR", "USA", "UMI", "URY", "UZB", "VUT", "VAT", "VEN", "VNM", "WLF", "ESH", "YEM", "ZMB", "ZWE"};

    public static final int FFP_TIER_BLUE = 1;
    public static final int FFP_TIER_SILVER = 2;
    public static final int FFP_TIER_GOLD = 3;
    public static final String FFP_TIER_BLUE_LABEL = "Elite Blue";
    public static final String FFP_TIER_SILVER_LABEL = "Elite Silver";
    public static final String FFP_TIER_GOLD_LABEL = "Elite Gold";

    public static final String[] FFP_ALLIANCE_LIST_CODE = {"MA", "A3", "AC", "CA", "AI", "NZ", "NH", "OZ", "AV", "O6", "CM", "MS", "ET", "BR", "B6", "LH", "SK", "SQ", "SA", "JJ", "TP", "TG", "TK", "UA", "VX", "VS", "VA", "UK"};
    public static final String[] FFP_ALLIANCE_LIST_NAME = {"Merlion Airlines - MerlionFlyer Elite", "Aegean Airlines - Miles&Bonus", "Air Canada - Aeroplan", "Air China / Shenzhen Airlines - PhoenixMiles", "Air India - Flying Returns", "Air New Zealand - AirPoints", "ANA - ANA Mileage Club", "Asiana Airlines - Asiana Club", "Avianca - LifeMiles", "Avianca Brazil - Amigo", "Copa Airlines - ConnectMiles", "EgyptAir - EgyptAir Plus", "Ethiopian Airlines - ShebaMiles", "EVA Air - Infinity MileageLands", "JetBlue - TrueBlue", "Lufthansa - Miles & More", "Scandinavian Airlines - EuroBonus", "Singapore Airlines - KrisFlyer", "South African Airways - Voyager", "TAM Airlines - Fidelidade", "TAP Portugal - Victoria", "THAI - Royal Orchid Plus", "Turkish Airlines - Miles&Smiles", "United - MileagePlus", "Virgin America - Elevate", "Virgin Atlantic - Flying Club", "Virgin Australia - Velocity", "Vistara - Club Vistara"};

    public static final String[] COSTS = {"Consumables Per Flight", "Aircraft One-Time Cost", "Cost Per Maintenance", "Market Fuel Cost", "Annual Cost"};

    public static final int COST_PER_FLIGHT = 0;
    public static final int COST_PER_AIRCRAFT = 1;
    public static final int COST_PER_MAINTENANCE = 2;
    public static final int COST_FUEL = 3;
    public static final int COST_ANNUAL = 4;

    public static final String FARE_NORMAL = "NNMA";
    public static final String FARE_LATE = "LTH";
    public static final String FARE_DOUBLE = "GV2";
    public static final String FARE_EARLY = "SVR1";
    public static final String FARE_EXPENSIVE = "FE7";

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

    public static final int RANGE_CONST = 300;
    public static final double RANGE_MOMENTUM = 0.85;
    public static final double OPERATIONAL_RANGE = 0.6;

    public static final String SSR_ACTION_CODE_TICKET_NUMBER = "TKNR";
    public static final String SSR_ACTION_CODE_FFP = "FFP";

    public static int MAX_CONNECTION_TIME_MINUTES = 1440;
}
