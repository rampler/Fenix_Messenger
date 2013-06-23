package cryptography;

interface Algorythm {
	public String crypt(String text, String key);
	public String decrypt(String text, String key);
}
