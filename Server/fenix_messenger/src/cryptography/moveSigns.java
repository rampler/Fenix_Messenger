package cryptography;

public class moveSigns implements Algorythm {

	private int getIntFromKey(String key)
	{
		int intKey = 0;
		for(int i=0; i<key.length(); i++) intKey += (int)key.charAt(i);
		intKey %= 10;
		return intKey+1;
	}
	
	public String crypt(String text, String key) 
	{
		int intKey = getIntFromKey(key);
		String temp = "";
		for(int i=0; i<text.length(); i++) 
		{
			int znak = (int)text.charAt(i);
			if(znak >= 48 && znak <= 57) 
			{
				int control = (text.charAt(i)+intKey)%58;
				if(control >= 48) temp += (char)((text.charAt(i)+intKey));
				else temp += (char)((48+control));
			}
			else if(znak >= 65 && znak <= 90) 
			{
				int control = (text.charAt(i)+intKey)%91;
				if(control >= 65) temp += (char)((text.charAt(i)+intKey));
				else temp += (char)((65+control));
			}
			else if(znak >= 97 && znak <= 122) 
			{
				int control = (text.charAt(i)+intKey)%123;
				if(control >= 97) temp += (char)((text.charAt(i)+intKey));
				else temp += (char)((97+control));
			}
			else temp += text.charAt(i);
		}
		return temp;
	}
	
	public String decrypt(String text, String key) 
	{
		int intKey = getIntFromKey(key);
		String temp = "";
		for(int i=0; i<text.length(); i++) 
		{
			int znak = (int)text.charAt(i);
			if(znak >= 48 && znak <= 57) 
			{
				int control = znak-intKey-48;
				if(control >= 0) temp += (char)((text.charAt(i)-intKey));
				else temp += (char)((58+control));
			}
			else if(znak >= 65 && znak <= 90) 
			{
				int control = znak-intKey-65;
				if(control >= 0) temp += (char)((text.charAt(i)-intKey));
				else temp += (char)((91+control));
			}
			else if(znak >= 97 && znak <= 122) 
			{
				int control = znak-intKey-97;
				if(control >= 0) temp += (char)((text.charAt(i)-intKey));
				else temp += (char)((123+control));
			}
			else temp += text.charAt(i);
		}
		return temp;
	}

}
