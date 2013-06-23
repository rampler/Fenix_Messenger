package cryptography;

public class Cryptographer {
	private Algorythm algorythm;
	
	public Cryptographer(Algorythm _algorythm){ algorythm = _algorythm;	}

	public String cryptString(String text, String key)
	{
		return algorythm.crypt(text, key);
	}
	
	public String decryptString(String text, String key)
	{
		return algorythm.decrypt(text, key);
	}
}
