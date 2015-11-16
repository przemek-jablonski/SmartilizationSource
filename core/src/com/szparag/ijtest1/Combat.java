package com.szparag.ijtest1;

import java.util.Random;

/**
 * Created by Szparagowy Krul 3000 on 29/05/2015.
 */
public class Combat {

    Hex[][] hexmap;
    Random random;
    Toolbox toolbox;

    public Combat(Hex[][] hexmap){
        this.hexmap = hexmap;
     // random = new Random(TIME IN MILIS?);
        random = new Random();
        toolbox = new Toolbox();
    }

    public void closequarters(Actor attacker, Actor defendant){
        System.out.println("closequarters combat");
        if(attacker instanceof ActorUnit
                && (attacker.playerenum != defendant.playerenum)) {
            if(defendant instanceof ActorBuilding) closequarters_building((ActorUnit)attacker, (ActorBuilding)defendant);
            else if(defendant instanceof ActorUnit) closequarters_unit((ActorUnit)attacker, (ActorUnit)defendant);
                else System.out.println("no znowu cos zjebales ;/");

        } else System.out.println("attacker is not an actorUnit, cos ty odjebal najlepszego?!");
    }

    public void rangedcombat(Actor attacker, Actor defendant){
        System.out.println("rangedcombat");
        if(attacker instanceof ActorUnit
                && (attacker.playerenum != defendant.playerenum)) {
            if(defendant instanceof ActorBuilding) ranged_building((ActorUnit) attacker, (ActorBuilding) defendant);
            else if(defendant instanceof ActorUnit) ranged_unit((ActorUnit) attacker, (ActorUnit) defendant);
            else System.out.println("no znowu cos zjebales ;/");

        } else System.out.println("attacker is not an actorUnit, cos ty odjebal najlepszego?!");
    }

    private void closequarters_unit(ActorUnit attacker, ActorUnit defendant){
        System.out.println("closequarters_unit dmg:");
        //designed in a way to make sure that damage for standard combat unit
        //(that is with attackmod = 1)
        //had a damage deal of roughly between 10 and 55 points along with increasing for exp points
        //defendant has a chance to deflect a hit, with power of 5 to 15
        //increasing with ATTACKER's EXPERIENCE POINTS with higher ratio
        //to balance out a situation of significantly stronger attacker.
        float hit = attacker.attackModifier * (random.nextInt(40)+20);
        float atkexp = toolbox.logbase5int((int)attacker.expPoints) * (attacker.expPoints / 15);
        float def = defendant.defendModifier * (random.nextInt(11)+5);
        float defexp = toolbox.logbase5int((int)defendant.expPoints) * (attacker.expPoints / 5);
        float damage = hit + atkexp - def - defexp;
        System.out.println("hit: " + hit + " atkexp: " + atkexp + " def: " + def + " defexp: " + defexp);
        System.out.println("total damage dealt: " + damage);
      //  if(!(damage > defendant.hitPoints))
            defendant.decreaseHitPoints(Math.abs((int)(hit+atkexp-def-defexp)));
    }

    private void closequarters_building(ActorUnit attacker, ActorBuilding defendant){
        System.out.println("combat: CC_building");
    }

    private void ranged_unit(ActorUnit attacker, ActorUnit defendant){
        System.out.println("combat: ranged_unit");
        //ranged attacks when attacking an unit are way more random than closequarter ones
        //but offer much higher exppoints ratio and can deal +10 dmg tops
        //also, defender exp point modifier is calculated from DEFENDING unit exp
        //and not attacking unit
        float hit = attacker.rangedAttackModifier * (random.nextInt(55)+7);
        float atkexp = toolbox.logbase5int((int)attacker.expPoints) * (attacker.expPoints / 5);
        float def = defendant.getRangedDefendModifier() * (random.nextInt(11)+5);
        float defexp = toolbox.logbase5int((int)defendant.expPoints) * (defendant.expPoints / 5);
        float damage = hit + atkexp - def - defexp;
        System.out.println("hit: " + hit + " atkexp: " + atkexp + " def: " + def + " defexp: " + defexp);
        System.out.println("total ranged damage dealt: " + damage);
        if(!(damage > defendant.hitPoints))
            defendant.decreaseHitPoints((int)(hit+atkexp-def-defexp));
    }

    private void ranged_building(Actor attacker, Actor defendant){
        System.out.println("combat: ranged_building");
    }

}
