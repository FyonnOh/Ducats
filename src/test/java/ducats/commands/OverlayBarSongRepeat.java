package ducats.commands;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ducats.Parser;
import ducats.commands.AddBarCommand;
import ducats.commands.NewCommand;
import ducats.components.SongList;
import ducats.commands.OverlayGroupGroup;

import ducats.Ui;
import java.nio.file.Paths;
import java.util.ArrayList;
import ducats.components.Song;
import ducats.commands.OverlayBarSong;
import ducats.components.SongConverter;

public class OverlayBarSongRepeat {
    @Test

    //this test case tests when overlaying 1 bar onto another

    public void testGroupGroup() {

        // ("/home/rishi/Desktop/cs2113t/team/main/data/todo_list" +".txt"));
        String fileDelimiter = System.getProperty("file.separator");
        ducats.Storage storage = new ducats.Storage(System.getProperty("user.dir") + fileDelimiter + "data");
        //ducats.Storage storage = new ducats.Storage(Paths.get("data", "songlist.txt"));
        SongList songs = new SongList();
        SongConverter songconverter = new SongConverter();
        String testSong = "Twinkle aminor 120 [[UAs],[UA],[UAs],[UA],[UAs],[UA],[UAs],[UA]] "
                + "[[UBs],[UB],[UBs],[UB],[UBs],[UB],[UBs],[UB]] "
                + "[[UCs],[UC],[UCs],[UC],[UCs],[UC],[UCs],[UC]] "
                + "[[UDs],[UD],[UDs],[UD],[UDs],[UD],[UDs],[UD]] "
                + "[[UEs],[UE],[UEs],[UE],[UEs],[UE],[UEs],[UE]] "
                + "[[UFs],[UF],[UFs],[UF],[UFs],[UF],[UFs],[UF]] "
                + "[[UGs],[UG],[UGs],[UG],[UGs],[UG],[UGs],[UG]] ";
        String testSong2 = "Twinkle1 aminor 120 [[UBs],[UB],[UBs],[UB],[UBs],[UB],[UBs],[UB]] "
                + "[[UCs],[UC],[UCs],[UC],[UCs],[UC],[UCs],[UC]] "
                + "[[UDs],[UD],[UDs],[UD],[UDs],[UD],[UDs],[UD]] "
                + "[[UAs],[UA],[UAs],[UA],[UAs],[UA],[UAs],[UA]] "
                + "[[UFs],[UF],[UFs],[UF],[UFs],[UF],[UFs],[UF]] "
                + "[[UGs],[UG],[UGs],[UG],[UGs],[UG],[UGs],[UG]] "
                + "[[UEs],[UE],[UEs],[UE],[UEs],[UE],[UEs],[UE]] ";
        try {
            songs.add(songconverter.convertSongFromString(testSong));
            songs.add(songconverter.convertSongFromString(testSong2));
        } catch (Exception e) {
            System.out.println(testSong);
            songs = new SongList();
        }
        String input = "{UPPER_A}{UPPER_A}{UPPER_A}{UPPER_A}|"
                + "{UPPER_B}{UPPER_B}{UPPER_B}{UPPER_B}|"
                + "{UPPER_C}{UPPER_C}{UPPER_C}{UPPER_C}|"
                + "{UPPER_D }{UPPER_D }{UPPER_D }{UPPER_D }|"
                + "{UPPER_E }{UPPER_E }{UPPER_E }{UPPER_E }|"
                + "{UPPER_F }{UPPER_F }{UPPER_F }{UPPER_F }|{UPPER_G }{UPPER_G }{UPPER_G }{UPPER_G }|";


        Ui ui = new Ui();
        ducats.commands.OpenCommand open = new ducats.commands.OpenCommand("open Twinkle");
        try {
            open.execute(songs,ui,storage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ducats.commands.GroupCommand grouper = new ducats.commands.GroupCommand("group 1 3 twinkle1");
        ducats.commands.GroupCommand grouper1 = new ducats.commands.GroupCommand("group 2 4 twinkle2");
        try {
            grouper.execute(songs,ui,storage);
            grouper1.execute(songs,ui,storage);
        } catch (Exception e) {
            //System.out.print("addsadsa");
            System.out.print(e);
        }
        String expected =  "{UPPER_B }{UPPER_B }{UPPER_B }{UPPER_B }|"
                + "{UPPER_C UPPER_A }{UPPER_C UPPER_A }{UPPER_C UPPER_A }{UPPER_C UPPER_A }|"
                + "{UPPER_D UPPER_A }{UPPER_D UPPER_A }{UPPER_D UPPER_A }{UPPER_D UPPER_A }|"
                + "{UPPER_A UPPER_A }{UPPER_A UPPER_A }{UPPER_A UPPER_A }{UPPER_A UPPER_A }|"
                + "{UPPER_F UPPER_A }{UPPER_F UPPER_A }{UPPER_F UPPER_A }{UPPER_F UPPER_A }|"
                + "{UPPER_G UPPER_A }{UPPER_G UPPER_A }{UPPER_G UPPER_A }{UPPER_G UPPER_A }|"
                + "{UPPER_E UPPER_A }{UPPER_E UPPER_A }{UPPER_E UPPER_A }{UPPER_E UPPER_A }|";

        OverlayBarSong tester = new OverlayBarSong("overlay_bar_song Twinkle 1 Twinkle1 2 repeat");
        try {
            tester.execute(songs,ui,storage);
        } catch (Exception e) {

            System.out.println(e);
        }
        //ducats.components.SongList songList = new SongList();
        ArrayList<ducats.components.Song> findList = songs.findSong("Twinkle1");
        assertEquals(expected,findList.get(0).showSongChart());
    }
}  
