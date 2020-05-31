package eh.com.musicsdk.data;

public class Music {

    String name;
    String artist;
    String album;
    String filePath;


    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getArtist ( ) {
        return artist;
    }

    public void setArtist (String artist) {
        this.artist = artist;
    }

    public String getAlbum ( ) {
        return album;
    }

    public void setAlbum (String album) {
        this.album = album;
    }

    public String getFilePath ( ) {
        return filePath;
    }

    public void setFilePath (String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString ( ) {
        return "Music{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
