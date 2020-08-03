package Animation;

import java.util.ArrayList;

public class Quadtree {
	
	  private Rectangle boundary;
	  private ArrayList<Person> points;

	  private Quadtree[] divisions;

	  private int limit;

	  boolean divided;

	  Quadtree(Rectangle boundary, ArrayList<Person> population, int limit) {
	    this.boundary = boundary;
	    this.points = new ArrayList<Person>();
	    this.limit = limit;
	    this.divided = false;

	    for (int i = 0; i < population.size(); i++) {
	      this.insert(population.get(i));
	    }
	  }


	  // We will think of the indices of the subdivisions as numbering coordinates on a graph - 1 (counterclockwise starting with zero)
	  void subdivide() {

	    this.divided = true;

	    // make new boundaries
	    Rectangle[] boundaries = new Rectangle[4];
	    boundaries[0] = new Rectangle(boundary.x + boundary.w/2, boundary.y, boundary.w/2, boundary.h/2);
	    boundaries[1] = new Rectangle(boundary.x, boundary.y, boundary.w/2, boundary.h/2);
	    boundaries[2] = new Rectangle(boundary.x, boundary.y + boundary.h/2, boundary.w/2, boundary.h/2);
	    boundaries[3] = new Rectangle(boundary.x + boundary.w/2, boundary.y + boundary.h/2, boundary.w/2, boundary.h/2);
	    divisions = new Quadtree[4];
	    for (int i = 0; i < divisions.length; i++) {
	      divisions[i] = new Quadtree(boundaries[i], points, limit);
	    }
	  }



	  void insert(Person p) {

	    if (!this.boundary.contains(p)) {
	      return;
	    }


	    if (points.size() < limit) {
	      points.add(p);
	    } else {
	      if (!this.divided) {
	        subdivide();
	      }
	      for (Quadtree q : divisions) {
	        q.insert(p);
	      }
	    }
	  }


//	  void show() {
//	    stroke(255);
//	    //text(points.size(), boundary.x + 10, boundary.y + 10);
//	    strokeWeight(2);
//	    noFill();
//	    rect(boundary.x, boundary.y, boundary.w, boundary.h);
//	    if (this.divided) {
//	      for (int i = 0; i < divisions.length; i++) {
//	        divisions[i].show();
//	      }
//	    }
//	  }

	  ArrayList<Person> query(Rectangle range) {
	    ArrayList<Person> found = new ArrayList<Person>();
	    if (!this.boundary.intersects(range)) {
	      return found;
	    } else {
	      for (Person p : points) {
	        if (range.contains(p)) {
	          found.add(p);
	        }
	      }

	      if (this.divided) {
	        for (Quadtree q : divisions) {
	          found.addAll(q.query(range));
	        }
	      }

	      return found;
	    }
	  }

}
