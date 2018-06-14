package com.gmb.mycalcolibrary.tools;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author GMBcyr
 */
public class CongruLine {

 
long pos;
long rb;
long restePrecedant;
long riCi;
long d;
long qi;
long nqi;
long r;
long nombre;

public CongruLine(){}

public CongruLine(long i, long ri, long c, long riCi, long d, long qi, long nqi, long r, long nombre){
    
    this.nombre=nombre;
   this.pos=i;
   this.rb=ri;
   this.restePrecedant=c;
   this.riCi=riCi;
   this.d=d;
   this.qi=qi;
   this.nqi=nqi;
   this.r=r;
   
}

    public long getD() {
        return d;
    }

    public void setD(long d) {
        this.d = d;
    }

    public long getNqi() {
        return nqi;
    }

    public void setNqi(long nqi) {
        this.nqi = nqi;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public long getQi() {
        return qi;
    }

    public void setQi(long qi) {
        this.qi = qi;
    }

    public long getR() {
        return r;
    }

    public void setR(long r) {
        this.r = r;
    }

    public long getRb() {
        return rb;
    }

    public void setRb(long rb) {
        this.rb = rb;
    }

    public long getRestePrecedant() {
        return restePrecedant;
    }

    public void setRestePrecedant(long restePrecedant) {
        this.restePrecedant = restePrecedant;
    }

    public long getRiCi() {
        return riCi;
    }

    public void setRiCi(long riCi) {
        this.riCi = riCi;
    }

    public long getNombre() {
        return nombre;
    }

    public void setNombre(long nombre) {
        this.nombre = nombre;
    }


    
}

