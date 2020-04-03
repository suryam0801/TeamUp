package com.example.teamup;

public class projects {

    String pname;
    String pdesc;
    String pid;

    public projects() {
    }

    public projects(String pname, String pdesc, String pid) {
        this.pname = pname;
        this.pdesc = pdesc;
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}

