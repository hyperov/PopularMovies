package pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DELL I7 on 10/11/2015.
 */
public class Movie implements Parcelable {

    private String title;
    private String img;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String id;

    private Movie() {

    }

    public Movie(String title, String img, String plotSynopsis, String userRating, String releaseDate, String id) {
        this.setId(id);
        this.setTitle(title);
        this.setImg(img);
        this.setPlotSynopsis(plotSynopsis);
        this.setReleaseDate(releaseDate);
        this.setUserRating(userRating);

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    Movie(Parcel in) {

        //this.setId(id);
        this.setId(in.readString());
        this.setImg(in.readString());
        this.setTitle(in.readString());
        this.setUserRating(in.readString());
        this.setPlotSynopsis(in.readString());
        this.setReleaseDate(in.readString());


    }

    @Override
    public void writeToParcel(Parcel dest,int parcelFlags) {
        dest.writeString(this.getId());
        dest.writeString(this.getImg());
        dest.writeString(this.getTitle());
        dest.writeString(this.getUserRating());
        dest.writeString(this.getPlotSynopsis());
        dest.writeString(this.getReleaseDate());


    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
