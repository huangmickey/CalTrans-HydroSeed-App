package com.example.hydroseed;

public class FileContentItem {
    private String header,
            r1_c1, r1_c2, r1_c3,
            r2_c1, r2_c2, r2_c3,
            r3_c1, r3_c2, r3_c3,
            r4_c1, r4_c2, r4_c3,
            r5_c1, r5_c2, r5_c3;

    //Constructor Class for cardView Layout cardview_file_contents.xml
    public FileContentItem(String header,
                           String r1_c1, String r1_c2, String r1_c3,
                           String r2_c1, String r2_c2, String r2_c3,
                           String r3_c1, String r3_c2, String r3_c3,
                           String r4_c1, String r4_c2, String r4_c3,
                           String r5_c1, String r5_c2, String r5_c3
                           ) {
        this.header = header;

        this.r1_c1 = r1_c1;
        this.r1_c2 = r1_c2;
        this.r1_c3 = r1_c3;

        this.r2_c1 = r2_c1;
        this.r2_c2 = r2_c2;
        this.r2_c3 = r2_c3;

        this.r3_c1 = r3_c1;
        this.r3_c2 = r3_c2;
        this.r3_c3 = r3_c3;

        this.r4_c1 = r4_c1;
        this.r4_c2 = r4_c2;
        this.r4_c3 = r4_c3;

        this.r5_c1 = r5_c1;
        this.r5_c2 = r5_c2;
        this.r5_c3 = r5_c3;

    }
    //Get Methods for cardView layout

    public String getHeader() {
        return header;
    }

    public String getR1_c1() {
        return r1_c1;
    }

    public String getR1_c2() {
        return r1_c2;
    }

    public String getR1_c3() {
        return r1_c3;
    }

    public String getR2_c1() {
        return r2_c1;
    }

    public String getR2_c2() {
        return r2_c2;
    }

    public String getR2_c3() {
        return r2_c3;
    }

    public String getR3_c1() {
        return r3_c1;
    }

    public String getR3_c2() {
        return r3_c2;
    }

    public String getR3_c3() {
        return r3_c3;
    }

    public String getR4_c1() {
        return r4_c1;
    }

    public String getR4_c2() {
        return r4_c2;
    }

    public String getR4_c3() {
        return r4_c3;
    }

    public String getR5_c1() {
        return r5_c1;
    }

    public String getR5_c2() {
        return r5_c2;
    }

    public String getR5_c3() {
        return r5_c3;
    }
}
