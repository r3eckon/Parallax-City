package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.Random;

/**
 * Created by R3VISION on 6/22/2016.
 */
public class TheCity {

    /*
       The City

     */
    City city;
    int ground = 0;
    float spd = 1;
    boolean auto = false;
    long seed = 123;

    public TheCity(){
        city = new City( 7 , seed , 25000);
        init();
    }

    public void init(){
        for(int i = 0 ; i < city.layers ; i++){
            city.fillbuffer(i);
        }
    }


    public void update(){

        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)){
            auto = !auto;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            this.city = new City(7 , seed++ , 25000);
            init();
        }
        if(auto){
            for(int i = 0 ; i < city.layers ; i++){
                    city.bufferpos[i]-=(MyGdxGame.camera.zoom)*spd*(1-(i*city.parralax));
                    city.fillbuffer(i);

            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            for(int i = 0 ; i < city.layers ; i++) {
                city.bufferpos[i] -= (MyGdxGame.camera.zoom) * (spd / (Gdx.graphics.getDeltaTime())) * (1 - (i * city.parralax));
                city.fillbuffer(i);
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            for(int i = 0 ; i < city.layers ; i++) {
                city.bufferpos[i] += (MyGdxGame.camera.zoom) * (spd / (Gdx.graphics.getDeltaTime())) * (1 - (i * city.parralax));
                city.fillbuffer(i);
            }
        }



    }

    public void render(ShapeRenderer sr){


        //Water
        sr.begin();

        sr.set(ShapeRenderer.ShapeType.Filled);



        sr.setColor(Color.DARK_GRAY);
        sr.rect(city.bufferpos[0]-Gdx.graphics.getWidth()*MyGdxGame.camera.zoom , ground - 3220 , Gdx.graphics.getWidth()*MyGdxGame.camera.zoom*2 , -300);
        sr.end();





        sr.begin();
        Color black = Color.BLACK.sub(new Color(0 , 0 , 0 , 0f));
        Color blue = Color.BLUE.sub(new Color(0 , 0 , 0 , 0f));
        sr.rect(city.bufferpos[0]-Gdx.graphics.getWidth()*MyGdxGame.camera.zoom , ground - 2000 , Gdx.graphics.getWidth()*MyGdxGame.camera.zoom*2 , -Gdx.graphics.getHeight()*MyGdxGame.camera.zoom, black ,black , blue , blue);
        sr.end();



        for(int i = city.layers ; i > 0; i--){
            city.drawbuffer(sr , i-1);
        }



    }






}
class Building{

    int layer;//position in city relative to camera

    int rows , columns , margin , wwidth , wheight;//Windows
    double tint;
    float tintf;
    int[][] windows;
    int roof;

    public Building(int layer){
        this.layer = layer;
    }
    public int generate(int layer , long sd , int index){
        long seed = (layer+1)*((sd*index)+(index/sd));

        Random random = new Random(seed);

        if(random.nextInt(10000) % (200 + (500*(layer*layer))) != 0) return 0;

        columns = random.nextInt(5*(layer+1))+2*layer;
        rows = random.nextInt((5+2*layer)*(layer+1)+random.nextInt(2)*layer);

        if(columns == 0 || rows == 0) return 0;

        if(random.nextInt(50) < 10){
            int t = columns;
            columns = rows;
            rows = t;
        }
        tintf = ((layer+1)*0.02f);
        margin = 50;
        wwidth = 100;
        wheight = 100;
        windows = new int[columns][rows];
        roof = random.nextInt((layer+1)*30);

        for(int i = 0 ; i < columns ; i++){
            here:
            for(int j = 0 ; j < rows ; j++){
                if(windows[i][j] != 0) continue;
                windows[i][j] = (random.nextInt(100) < 10) ? 1 : 0;
                if(random.nextInt(100) < 1){
                    for(int w = 0 ; w < random.nextInt((columns)) ; w++ ){
                        if(i+w < columns){
                            windows[i+w][j] = 1;
                        }else{
                            break here;
                        }

                    }

                }
            }
        }

        return 1;
    }
    public void draw(ShapeRenderer sr , int x , int y , float p , Color tint){
        y = -MyGdxGame.HEIGHT*3;
        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0.14f-tintf , 0.14f-tintf , 0.14f-tintf , 0).add(tint));
        sr.rect(x+(p*layer*MyGdxGame.camera.position.x) , y , columns*(wwidth+margin)+ margin, rows*(wheight+margin) + margin + roof);



        for(int i = 0 ; i < columns ; i++){
            for(int j = 0 ; j < rows ; j++){

                Color c = (windows[i][j] == 0) ? (Color.SKY.cpy().sub(tintf*12, tintf*12 , tintf*12 , 0)) :(Color.YELLOW.cpy().sub(tintf*2, tintf*2 , tintf*2 , 0));
                sr.setColor(c);
                sr.rect(((x + margin + (i*(wwidth+margin))))+((p*layer*MyGdxGame.camera.position.x)) ,y + margin +( j*(wheight+margin)) , wwidth, wheight);
            }
        }

        
        sr.end();

    }

    public void drawReflection(ShapeRenderer sr , int x , int y , float p , Color tint){
        //Reflection


        sr.begin();
        sr.set(ShapeRenderer.ShapeType.Filled);

        sr.setColor(new Color(0.18f-tintf , 0.18f-tintf , 0.18f-tintf , 0.5f));
        sr.rect(x+(p*layer*MyGdxGame.camera.position.x) , y - 3300 , columns*(wwidth+margin)+ margin, -(rows*(wheight+margin) + margin + roof));

        for(int i = 0 ; i < columns ; i++){
            for(int j = 0 ; j < rows ; j++){

                Color c = (windows[i][j] == 0) ? (Color.SKY.cpy().sub(tintf*6, tintf*6 , tintf*6 , 0.5f)) :(Color.YELLOW.cpy().sub(tintf*2, tintf*2 , tintf*2 , 0.5f));
                sr.setColor(c);
                sr.rect(((x + margin + (i*(wwidth+margin))))+((p*layer*MyGdxGame.camera.position.x)) ,-(y + margin +( j*(wheight+margin))) - 3300 , wwidth, -wheight);
            }
        }

        sr.end();



    }


}
class City{

    Building[][] buffer;
    public int[] bufferpos;
    public Color[] layertint;
    int layers , buffersize , indexoffset;
    int ground;
    float parralax;
    long seed;


    public City(int l , long s , int bs){
        this.layers = l;
        this.seed = s;
        this.buffersize = bs;
        this.buffer = new Building[layers][buffersize*2+1];
        this.bufferpos = new int[layers];
        this.layertint = new Color[layers];
        this.indexoffset = 0;
        this.parralax = 0.1f;
        this.ground = 0;


        Random rand = new Random(s);

        for(int i = 0 ; i < layers ; i++){
            bufferpos[i] = 0;
            layertint[i] = new Color(rand.nextFloat() , rand.nextFloat() , rand.nextFloat() , 0).mul(0.1f);
        }

    }
    public void fillbuffer(int layer){
        this.buffer[layer] = new Building[buffersize*2+1];
        for(int i = -buffersize ; i < buffersize ; i++){
            int bufferindex = i+buffersize;
            int realindex = (bufferpos[layer]+i);

            Building b = get(realindex , layer);
            if(b != null){
                buffer[layer][bufferindex] = b;
            }

        }
    }
    public void drawbuffer(ShapeRenderer sr , int layer){
        for(int i = -buffersize ; i < buffersize ; i++){
            int bufferindex = i+buffersize;
            int realindex = (bufferpos[layer]+i);
            if(buffer[layer][bufferindex]!=null){
                buffer[layer][bufferindex].draw(sr,realindex , ground , parralax , layertint[layer]);
                //buffer[layer][bufferindex].drawReflection(sr,realindex , ground , parralax , layertint[layer]);
            }
        }
    }

    public Building get(int index , int layer){
        Building b = new Building(layer);
        indexoffset = b.generate(layer,seed,index);
        return (indexoffset == 0) ? null : b;

    }

}
