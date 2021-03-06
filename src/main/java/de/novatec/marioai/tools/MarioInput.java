package de.novatec.marioai.tools;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import ch.idsia.mario.environments.Environment;

/**
 * Represents the state of pressed keys by the agent.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class MarioInput {
	
	public boolean[] toArray() {
		boolean[] action=new boolean[Environment.numberOfButtons];
		
		for(MarioKey key:getPressed()) action[key.getCode()]=true;
		
		return action;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pressed == null) ? 0 : pressed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarioInput other = (MarioInput) obj;
		if (pressed == null) {
			if (other.pressed != null)
				return false;
		} else 
			for(MarioKey next:getPressed()) {
				if(!((MarioInput)obj).isPressed(next)) return false;
			}
		return true;
	}

	private Set<MarioKey> justPressed = new TreeSet<MarioKey>(new Comparator<MarioKey>() {
		@Override
		public int compare(MarioKey o1, MarioKey o2) {
			return o1.getCode() - o2.getCode();
		}		
	});
	
	private Set<MarioKey> pressed = new TreeSet<MarioKey>(new Comparator<MarioKey>() {
		@Override
		public int compare(MarioKey o1, MarioKey o2) {
			return o1.getCode() - o2.getCode();
		}		
	});
	
	private Set<MarioKey> justReleased = new TreeSet<MarioKey>(new Comparator<MarioKey>() {
		@Override
		public int compare(MarioKey o1, MarioKey o2) {
			return o1.getCode() - o2.getCode();
		}		
	});
	
	/**
	 * New frame begins. Used to clear {@link #justPressed} and {@link #justReleased}.
	 */
	public void tick() {
		justPressed.clear();
		justReleased.clear();
	}
	
	/**
	 * Returns all pressed MarioKeys
	 * @return the set with all pressed MarioKeys
	 */
	public Set<MarioKey> getPressed() {
		return pressed;
	}
	
	/**
	 * Press or release given 'key'.
	 * @param key the MarioKey to change
	 * @param pressed true == PRESS, false == RELEASE
	 */
	public void set(MarioKey key, boolean pressed) {
		if (pressed) press(key);
		else release(key);
	}
	
	/**
	 * Change state of given 'key'.
	 * @param key the MarioKey to toggle
	 */
	public void toggle(MarioKey key) {
		if (pressed.contains(key)) pressed.remove(key);
		else pressed.add(key);
	}
	
	/**
	 * PRESS given 'key' or keep pressed if already pressed.
	 * @param key the MarioKey to press
	 */
	public void press(MarioKey key) {
		if (!pressed.contains(key)) {
			if (justReleased.contains(key)) {
				justReleased.remove(key);
			} else {
				justPressed.add(key);
			}
		}
		pressed.add(key);		
	}
	
	/**
	 * RELEASE given 'key'.
	 * @param key the MarioKey to release
	 */
	public void release(MarioKey key) {
		if (!pressed.contains(key)) return;
		pressed.remove(key);
		if (justPressed.contains(key)) {
			justPressed.remove(key);
		} else {
			justReleased.add(key);
		}
	}
	
	/**
	 * Whether 'key' is PRESSED.
	 * @param key the MarioKey to check 
	 * @return a boolean value indicating whether the given MarioKey is pressed
	 */
	public boolean isPressed(MarioKey key) {
		return pressed.contains(key);
	}
	
	/**
	 * Whether 'key' was NEWLY pressed THIS FRAME.
	 * @param key the MarioKey to check 
	 * @return a boolean value indicating whether the given MarioKey was newly pressed this frame
	 */
	public boolean isJustPressed(MarioKey key) {
		return justPressed.contains(key);
	}
	
	/**
	 * Whether 'key' was RELEASED THIS FRAME.
	 * @param key the MarioKey to check 
	 * @return a boolean value indicating whether the given MarioKey was newly released this frame
	 */
	public boolean isJustReleased(MarioKey key) {
		return justReleased.contains(key);
	}

	/**
	 * Completely resets the instance.
	 */
	public void reset() {
		justPressed.clear();
		justReleased.clear();
		pressed.clear();
	}
	
	@Override
	public String toString() {
		String result = "[";
		for (MarioKey marioKey : pressed) {
			result += marioKey.getName() + ",";
		}
		result += "]";
		return result;
	}
	
	
	/**
	 * Converts the given array to a useable MarioInput. 
	 * @param toInput Array that will be converted, size should be 5
	 * @return instance of MarioInput based on toInput 
	 */
	public static MarioInput arrayToMarioInput(boolean[] toInput) {
		MarioInput res=new MarioInput();
		
		if(toInput==null||toInput.length!=5) return res;
		
		if(toInput[0]) res.press(MarioKey.LEFT);
		if(toInput[1]) res.press(MarioKey.RIGHT);
		if(toInput[2]) res.press(MarioKey.DOWN);
		if(toInput[3]) res.press(MarioKey.JUMP);
		if(toInput[4]) res.press(MarioKey.SPEED);
		
		return res;
	}
	
}
