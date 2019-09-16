//* Jingyan Ma *//

import java.util.ArrayList;

public class MusicStore {
    //ADD YOUR CODE BELOW HERE
    private MyHashTable<String,Song> titlehash;
    private MyHashTable<String,ArrayList<Song>> artisthash;
    private MyHashTable<Integer,ArrayList<Song>> yearhash;

    //ADD YOUR CODE ABOVE HERE


    public MusicStore(ArrayList<Song> songs) {
        //ADD YOUR CODE BELOW HERE
        int size = songs.size();
        titlehash = new MyHashTable(size);
        artisthash = new MyHashTable(size);
        yearhash = new MyHashTable(size);
        for(Song s:songs){
            addSong(s);
        }
        //ADD YOUR CODE ABOVE HERE
    }


    /**
     * Add Song s to this MusicStore
     */
    public void addSong(Song s) {
        // ADD CODE BELOW HERE
        titlehash.put(s.getTitle(), s);

        if (artisthash.size() == 0) {
            ArrayList<Song> list1 = new ArrayList<Song>();
            list1.add(s);
            artisthash.put(s.getArtist(), list1);
            yearhash.put(s.getYear(), list1);

        } else {

            ArrayList<Song> list2 = new ArrayList<Song>();

            if (artisthash.get(s.getArtist()) != null) {
                artisthash.get(s.getArtist()).add(s);
            } else {
                list2.add(s);
                artisthash.put(s.getArtist(), list2);
            }
            ArrayList<Song> list3 = new ArrayList<Song>();
            if (yearhash.get(s.getYear()) != null) {
                yearhash.get(s.getYear()).add(s);
            } else {
                list3.add(s);
                yearhash.put(s.getYear(), list3);
            }
        }
        //ADD CODE ABOVE HERE
    }

    /*public void addSong(Song s) {
        // ADD CODE BELOW HERE
        titlehash.put(s.getTitle(),s);
        String artist = s.getArtist();
        if(artisthash.get(artist)==null){
            ArrayList<Song> list = new ArrayList<>();
            list.add(s);
            artisthash.put(artist,list);
        }else{
            artisthash.get(artist).add(s);
        }
        Integer year = s.getYear();
        if(yearhash.get(year)==null){
            ArrayList list2 = new ArrayList<>();
            list2.add(s);
            yearhash.put(year,list2);
        }else{
            yearhash.get(year).add(s);
        }
        // ADD CODE ABOVE HERE
    }*/


    /**
     * Search this MusicStore for Song by title and return any one song
     * by that title
     */
    public Song searchByTitle(String title) {
        //ADD CODE BELOW HERE
        return titlehash.get(title);
        //ADD CODE ABOVE HERE
    }

    /**
     * Search this MusicStore for song by `artist' and return an
     * ArrayList of all such Songs.
     */
    public ArrayList<Song> searchByArtist(String artist) {
        //ADD CODE BELOW HERE
        return artisthash.get(artist);
        //ADD CODE ABOVE HERE
    }

    /**
     * Search this MusicSotre for all songs from a `year'
     *  and return an ArrayList of all such  Songs
     */
    public ArrayList<Song> searchByYear(Integer year) {
        //ADD CODE BELOW HERE
        return yearhash.get(year);
        //ADD CODE ABOVE HERE

    }
}
