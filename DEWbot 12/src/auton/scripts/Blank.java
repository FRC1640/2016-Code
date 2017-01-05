package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;

public class Blank extends AutonScript { //blank script
	private static Blank blank;
	private ArrayList<AutonCommand> script = new ArrayList<AutonCommand>(){
		@Override
		public int indexOf(Object o){
			if (o == null) {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i) == null)
		                return i;
		    } else {
		        for (int i = 0; i < script.size(); i++)
		            if (script.get(i).equals(o))
		                return i;
		    }
			return -1;
		}
	};
	
	private Blank(){
	}
	
	public static Blank getInstance(){ //get singleton instance
		if(blank == null)
			blank = new Blank();
		return blank;
	}

	@Override
	public ArrayList<AutonCommand> getScript() { //return the blank script
		return script;
	}

}
