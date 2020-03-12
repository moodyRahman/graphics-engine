/**
 * Stores the RGB Values of a pixel
 */
public class Pixel {


private int red;
private int blue;
private int green;

public Pixel (int red, int green, int blue){
	this.red = red;
	this.green = green;
	this.blue = blue;

}

public int[] get(){
	int [] out = new int[3];
	out[0] = this.red;
	out[1] = this.green;
	out[2] = this.blue;
	return out;
}

public int getr(){
	return this.red;
}

public int getg(){
	return this.green;
}

public int getb(){
	return this.blue;
}

public String toString(){
	String out = "";

	out += this.red + " " + this.green + " " + this.blue + " ";
	return out;
}


public void set(Pixel p){
	this.red = p.get()[0];
	this.green = p.get()[1];
	this.blue = p.get()[2];
}

public void set(int r, int g, int b){
	this.red = r;
	this.green = g;
	this.blue = b;
}

}
