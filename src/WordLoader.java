import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WordLoader {

    /**
     * Loads a list of 5-letter words.
     * It first tries to load from the file "words/5letterWords.txt".
     * If the file is not found or is empty, it loads a default list of words.
     * 
     * @return A list of uppercase 5-letter words.
     */
    public static List<String> loadWords() {
        List<String> wordList = new ArrayList<>();
        // Note: For this path to work, you should have a "words" directory
        // in the root of your project, containing "5letterWords.txt".
        File file = new File("words/5letterWords.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toUpperCase();
                if (line.length() == 5) {
                    wordList.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            // File not found, will load defaults.
            System.out.println("Word file not found. Loading default words.");
        }

        // If the file was not found or was empty, load the default list.
        if (wordList.isEmpty()) {
            loadDefaultWords(wordList);
        }

        return wordList;
    }

    private static void loadDefaultWords(List<String> wordList) {
        String[] defaultWords = {
                "ABOUT", "ABOVE", "ACTOR", "ADMIT", "ADULT", "AFTER", "AGAIN", "AGENT", "AGREE", "AHEAD",
                "ALARM", "ALBUM", "ALERT", "ALIEN", "ALIKE", "ALIVE", "ALLOW", "ALONE", "ALONG", "AMONG",
                "APPLE", "APPLY", "ARENA", "ARGUE", "ARISE", "ASIDE", "AUDIO", "AVOID", "AWAKE", "AWARD",
                "BASIC", "BEACH", "BEGIN", "BEING", "BELOW", "BLACK", "BLANK", "BLOOD", "BOARD", "BRAIN",
                "BRAND", "BRAVE", "BREAD", "BREAK", "BRING", "BROAD", "BROWN", "BUILD", "BUILT", "CARRY",
                "CATCH", "CAUSE", "CHAIR", "CHART", "CHASE", "CHECK", "CHEST", "CHIEF", "CHILD", "CHINA",
                "CLAIM", "CLASS", "CLEAN", "CLEAR", "CLIMB", "CLOCK", "CLOSE", "CLOUD", "COACH", "COAST",
                "COUNT", "COURT", "COVER", "CRAFT", "CRASH", "CREAM", "CRIME", "CROSS", "CROWD", "DAILY",
                "DANCE", "DEATH", "DELAY", "DEPTH", "DOING", "DOUBT", "DRAFT", "DRAMA", "DREAM", "DRESS",
                "DRINK", "DRIVE", "EARLY", "EARTH", "EIGHT", "EMPTY", "ENEMY", "ENJOY", "ENTER", "EQUAL",
                "ERROR", "EVENT", "EVERY", "EXACT", "EXIST", "EXTRA", "FAITH", "FALSE", "FIELD", "FIGHT",
                "FINAL", "FIRST", "FLASH", "FLOOR", "FOCUS", "FORCE", "FOUND", "FRESH", "FRONT", "FRUIT",
                "FULLY", "GLASS", "GOING", "GRACE", "GRAND", "GRANT", "GRASS", "GREAT", "GREEN", "GROUP",
                "GROWN", "GUARD", "GUESS", "GUEST", "GUIDE", "HAPPY", "HEART", "HEAVY", "HORSE", "HOTEL",
                "HOUSE", "HUMAN", "IMAGE", "INDEX", "INNER", "INPUT", "ISSUE", "JAPAN", "JUDGE", "KNOWN",
                "LABEL", "LARGE", "LATER", "LAUGH", "LAYER", "LEARN", "LEAST", "LEAVE", "LEGAL", "LEVEL",
                "LIGHT", "LIMIT", "LINKS", "LIVES", "LOCAL", "LOGIC", "LOWER", "LUCKY", "LUNCH", "MAGIC",
                "MAJOR", "MARCH", "MATCH", "MAYBE", "MAYOR", "MEANT", "MEDIA", "METAL", "MIGHT", "MINOR",
                "MODEL", "MONEY", "MONTH", "MORAL", "MOTOR", "MOUNT", "MOUSE", "MOUTH", "MOVED", "MOVIE",
                "MUSIC", "NEEDS", "NEVER", "NIGHT", "NOISE", "NORTH", "NOTED", "NOVEL", "NURSE", "OCCUR",
                "OCEAN", "OFFER", "OFTEN", "ORDER", "OTHER", "OUGHT", "PAINT", "PANEL", "PAPER", "PARTY",
                "PEACE", "PHASE", "PHONE", "PHOTO", "PIANO", "PIECE", "PILOT", "PITCH", "PLACE", "PLAIN",
                "PLANE", "PLANT", "PLATE", "POINT", "POUND", "POWER", "PRESS", "PRICE", "PRIDE", "PRIME",
                "PRINT", "PRIOR", "PRIZE", "PROOF", "PROUD", "PROVE", "QUEEN", "QUICK", "QUIET", "QUITE",
                "RADIO", "RAISE", "RANGE", "RAPID", "RATIO", "REACH", "READY", "REFER", "RELAX", "REPLY",
                "RIGHT", "RIVAL", "RIVER", "ROBIN", "ROUGH", "ROUND", "ROUTE", "ROYAL", "RURAL", "SCALE",
                "SCENE", "SCOPE", "SCORE", "SENSE", "SERVE", "SEVEN", "SHALL", "SHAPE", "SHARE", "SHARP",
                "SHEET", "SHELF", "SHELL", "SHIFT", "SHINE", "SHIRT", "SHOCK", "SHOOT", "SHORT", "SHOWN",
                "SIGHT", "SINCE", "SIXTH", "SIXTY", "SKILL", "SLEEP", "SLIDE", "SMALL", "SMART", "SMILE",
                "SMOKE", "SOLID", "SOLVE", "SORRY", "SOUND", "SOUTH", "SPACE", "SPARE", "SPEAK", "SPEED",
                "SPEND", "SPLIT", "SPOKE", "SPORT", "STAFF", "STAGE", "STAKE", "STAND", "START", "STATE",
                "STEAM", "STEEL", "STICK", "STILL", "STOCK", "STONE", "STOOD", "STORE", "STORM", "STORY",
                "STUCK", "STUDY", "STUFF", "STYLE", "SUGAR", "SUPER", "SWEET", "TABLE", "TAKEN", "TASTE",
                "TEACH", "TEETH", "THANK", "THEIR", "THEME", "THERE", "THESE", "THICK", "THING", "THINK",
                "THIRD", "THOSE", "THREE", "THREW", "THROW", "TIGER", "TIGHT", "TIMES", "TIRED", "TITLE",
                "TODAY", "TOPIC", "TOTAL", "TOUCH", "TOUGH", "TOWER", "TRACK", "TRADE", "TRAIN", "TREAT",
                "TRIAL", "TRIBE", "TRUCK", "TRULY", "TRUST", "TRUTH", "TWICE", "UNCLE", "UNDER", "UNION",
                "UNTIL", "UPPER", "UPSET", "URBAN", "USAGE", "USUAL", "VALID", "VALUE", "VIDEO", "VIRUS",
                "VISIT", "VITAL", "VOICE", "WASTE", "WATCH", "WATER", "WHEEL", "WHERE", "WHICH", "WHILE",
                "WHITE", "WHOLE", "WHOSE", "WOMAN", "WOMEN", "WORLD", "WORRY", "WORSE", "WORST", "WORTH",
                "WOULD", "WRITE", "WRONG", "WROTE", "YOUNG", "YOUTH"
        };
        Collections.addAll(wordList, defaultWords);
    }
}