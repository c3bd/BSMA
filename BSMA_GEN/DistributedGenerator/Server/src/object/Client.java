package object;

import java.io.BufferedReader;
import java.io.DataOutputStream;

public class Client{
	BufferedReader is ;
	DataOutputStream os;
	public Client(BufferedReader is,DataOutputStream os){
		this.is=is;
		this.os=os;
	}
	public BufferedReader getIs() {
		return is;
	}
	public void setIs(BufferedReader is) {
		this.is = is;
	}
	public DataOutputStream getOs() {
		return os;
	}
	public void setOs(DataOutputStream os) {
		this.os = os;
	}
}