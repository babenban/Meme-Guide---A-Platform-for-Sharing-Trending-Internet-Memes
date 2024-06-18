package edu.hebut215054.bighomework1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Bean implements Parcelable {
    int bestcomment;
    int uid;
    String ptitle;
    String pcontext;
    int plike;
    int pid;
    String pusername;
    int cid;
    String tag;
    String picPath;

    protected Bean(Parcel in) {
        bestcomment = in.readInt();
        uid = in.readInt();
        ptitle = in.readString();
        pcontext = in.readString();
        plike = in.readInt();
        pid = in.readInt();
        pusername = in.readString();
        cid = in.readInt();
        tag = in.readString();
        picPath = in.readString();
    }

    public static final Creator<Bean> CREATOR = new Creator<Bean>() {
        @Override
        public Bean createFromParcel(Parcel in) {
            return new Bean(in);
        }

        @Override
        public Bean[] newArray(int size) {
            return new Bean[size];
        }
    };

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getBestcomment() {
        return bestcomment;
    }

    public void setBestcomment(int bestcomment) {
        this.bestcomment = bestcomment;
    }



    public String getPusername() {
        return pusername;
    }

    public void setPusername(String pusername) {
        this.pusername = pusername;
    }

    public Bean (){

    }



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPcontext() {
        return pcontext;
    }

    public void setPcontext(String pcontext) {
        this.pcontext = pcontext;
    }

    public int getPlike() {
        return plike;
    }

    public void setPlike(int plike) {
        this.plike = plike;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(bestcomment);
        dest.writeInt(uid);
        dest.writeString(ptitle);
        dest.writeString(pcontext);
        dest.writeInt(plike);
        dest.writeInt(pid);
        dest.writeString(pusername);
        dest.writeInt(cid);
        dest.writeString(tag);
        dest.writeString(picPath);
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
