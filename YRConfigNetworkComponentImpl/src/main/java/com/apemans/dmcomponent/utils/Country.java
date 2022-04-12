package com.apemans.dmcomponent.utils;

import com.tuya.smart.android.base.bean.CountryBean;

/**
 * @author Dylan Cai
 */
public class Country extends CountryBean {
    private String fr;
    private String it;
    private String de;
    private String es;
    private String ja;

    public Country() {
    }

    public String getFr() {
        return this.fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getIt() {
        return this.it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getDe() {
        return this.de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return this.es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getJa() {
        return this.ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }
}
