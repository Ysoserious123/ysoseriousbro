package Animation;

public class Rectangle {
	
	  double x;
	  double y;
	  float w;
	  float h;

	  Rectangle(double x, double y, float w, float h) {
	    this.x = x;
	    this.y = y;
	    this.w = w;
	    this.h = h;
	  }

	  boolean contains(Person p) {
	    return (p.getxCoord() > this.x && p.getxCoord() < this.x + this.w && p.getyCoord() > this.y && p.getyCoord() < this.y + this.h);
	  }

	  boolean intersects(Rectangle range) {
	    return !(range.x - range.w > this.x + this.w ||
	      range.x + range.w < this.x - this.w || 
	      range.y - range.h > this.y + this.h || 
	      range.y + range.h < this.y - this.h);
	  }

}
