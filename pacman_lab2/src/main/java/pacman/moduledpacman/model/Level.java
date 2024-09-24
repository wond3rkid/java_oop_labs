package pacman.moduledpacman.model;

enum Level {
    LEVEL_0(0,
            "#################################" +
                    "#EEEoOoEEEEEEEEE#EEEEEEEEEoOoEEE#" +
                    "#o###o#####E###E#E###E#####o###o#" +
                    "#E###E#EEE#E#E#E#E#E#E#EEE#E###E#" +
                    "#o###o#####E###E#E###E#####o###o#" +
                    "#oEoEEEEEEEoEEEEoEEEEoEEEEEEEoEo#" +
                    "#E###o#####o#########o#####o###E#" +
                    "#E###E#####EEE#####EEE#####E###E#" +
                    "#oEEEoEEEEEo#E#####E#oEEEEEoEEEo#" +
                    "######E####E#EEEEEEE#E####E######" +
                    "EEEEE#E####E#########E####E#EEEEE" +
                    "######E####E#EEEREEE#E####E######" +
                    "EEEEEEE####EEEEEEEEEEE####EEEEEEE" +
                    "######E####E##CCCCC##E####E######" +
                    "EEEEE#E####E##BEYEI##E####E#EEEEE" +
                    "EEEEE#E####E#########O####E#EEEEE" +
                    "EEEEE#oEEEoEEoEEoEEoEEoEEEo#EEEEE" +
                    "######E#########E#########E######" +
                    "#oEEEoEEoEEoEEE#o#EEEoEEoEEoEEEo#" +
                    "#E######E#####EEEEE#####E######E#" +
                    "#oEEEoE#E#####E###E#####E#EEEEEo#" +
                    "######o#EEEEEEE###EEEEEEE#o######" +
                    "######E#E#####E###E#####E#E######" +
                    "#oEEEoEoEEEEE#EEPEE#EEEEEoEoEEEo#" +
                    "#E##########E#######E##########E#" +
                    "#E##########E#######E##########E#" +
                    "#oEEEEo#EEEEEEEE#EEEEEEEE#oEEEEo#" +
                    "######O#E######E#E######E#O######" +
                    "#oEEEEo#E#EEEE#EEE#EEEE#E#oEEEEo#" +
                    "#E######E#E##E##E##E##E#E#E####E#" +
                    "#o######E#E##EEEEEEE##E#E#E####o#" +
                    "#EEEEEEEoEEEEo#####oEEEEoEEEEEEE#" +
                    "#################################"),
    LEVEL_1(1,
            "#################################" +
                    "#oooooooOoooooo###oooooooooooooo#" +
                    "#o####o#######o###o#######o####o#" +
                    "#o####o#######o###o#######o####o#" +
                    "#o####o#######o###o#######o####o#" +
                    "#oooooIoooooooooooooooooooBooooo#" +
                    "#o####o##o#############o##o####o#" +
                    "#o####o##o#############o##o####o#" +
                    "#oooooo##oooooo###oooooo##oooooo#" +
                    "######o#######o###o#######o######" +
                    "######o#######o###o#######o######" +
                    "######o##ooooooooooooooo##o######" +
                    "#oooooo##o######E######o##oooooo#" +
                    "#o#######o#EEEEEPEEEEE#o#######o#" +
                    "#oooooo##o#EEEEEEEEEEE#o##oooooo#" +
                    "######oooo#############oooo######" +
                    "#######o#o#ooooooooooo#o#o#######" +
                    "#######o#ooo#########ooo#o#######" +
                    "#######o#o#o#########o#o#o#######" +
                    "#######oooooooo###oooooooo#######" +
                    "#ooooooo######o###o######ooooooo#" +
                    "#o#####o######o###o######o#####o#" +
                    "#o#####ooooooooooooooooooo#####o#" +
                    "#ooo###o###o#########o###o###ooo#" +
                    "###o###o###o#########o###o###o###" +
                    "###o###ooooooooooooooooooo###o###" +
                    "#ooo###o###o#########o###o###ooo#" +
                    "#o####Yo###o#########o###oR####o#" +
                    "#o####o####ooooooooooo####o####o#" +
                    "#oooooo####o#########o####oooooo#" +
                    "#o######ooooooooooooooooo######o#" +
                    "#oooooooo###############oooooooo#" +
                    "#################################"),
    LEVEL_2(2, "#################################" +
            "#EEEEEEEEEEEEEoEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEoOoEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEoEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEIEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEREEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEBEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEYEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEPEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE#" +
            "#################################"),
    LEVEL_3(3, "#################################" +
            "#ooooooooooooooooooooooooooooooo#" +
            "#o#ooo###o#ooo#ooo####oo#oo#o#oo#" +
            "#o#o#o#ooo#ooo#ooo#oo#oo#ooooooo#" +
            "#o###o###o#ooo#oIo#oo#oo#o#####o#" +
            "#o#o#o#ooo#ooo#ooo#oo#oooooooooo#" +
            "#B#o#o###o###o###o####oo#ooooooo#" +
            "#ooooooooooooooooooooooooooooooo#" +
            "###############################o#" +
            "#ooooooooooooooooooooooooooooooo#" +
            "###########o####o####o###########" +
            "oooooooooooPo##oooooooooooooooooo" +
            "###########oo##o#####o###########" +
            "#ooooooooooooooooooooo###########" +
            "#o####oooo####o#######oooo#ooo#O#" +
            "#o#oo#ooo#oooooooooooooo#o#o#o#o#" +
            "#o#####oo####oo##########o#o#o#o#" +
            "#o#ooo#oo#oooooooooooooooo#o#o#o#" +
            "#o#####oo#####o############ooooo#" +
            "#ooooooooooooooooooooooooooooooo#" +
            "#o#ooooooooooooooooYoooooooooooo#" +
            "#o#oo#oo##oo###o###o#o#o#ooooooo#" +
            "#o#oo#o#oo#o#o#o#o#O#o#ooooooo##o" +
            "####o####o###o###oo#oo#ooooooo##o" +
            "#oo#o#oo#o#ooo#oooo#oooooooooo##o" +
            "#oo#o#oo#o#ooo#oooo#oo#ooooooo##o" +
            "#ooooooooooooooooooooooooooooo##o" +
            "oooo########o###oooooooooo######o" +
            "#####oooooooooo#ooo#####oooooo##o" +
            "oooO#R########o#ooo#o#o#oo###o##o" +
            "#####oooooooooo#ooo#o#o#oo#o#o##o" +
            "oooooo########oooooooooooooooo###" +
            "#################################");

    private final int number;
    private final String levelView;

    Level(int number, String levelView) {
        this.number = number;
        this.levelView = levelView;
    }

    public int getNumber() {
        return number;
    }

    public String getLevelView() {
        return levelView;
    }

    public static Level fromNumber(int number) {
        for (Level level : values()) {
            if (level.getNumber() == number) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid level number: " + number);
    }

    public static int getSize() {
        return values().length;
    }


}

