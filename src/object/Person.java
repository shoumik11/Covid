
package object;

public class Person {
	private String name;
	private int id;
	public void setId(int id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public Person(int id, String name) 
	{
		this.id = id;
		this.name = name;
	}
}
