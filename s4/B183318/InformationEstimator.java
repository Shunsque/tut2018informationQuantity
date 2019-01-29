package s4.B183318; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    
    boolean targetFlag = false; //If target is set, this flag becomes true.
	boolean spaceFlag = false; //If space is set, this flag becomes true. 

    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency

/*
    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }
*/
    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
	return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) {
    	myTarget = target;
    	if(target.length>0)
    		targetFlag=true;
    	}

    public void setSpace(byte []space) { 
		myFrequencer = new Frequencer();
		mySpace = space;
		myFrequencer.setSpace(space);
		spaceFlag=true; 
    }

    public double estimation(){
		if(targetFlag == false)
			return 0.0;
		if(spaceFlag == false)
			return Double.MAX_VALUE;

		myFrequencer.setTarget(myTarget);

		//Record the results of Information Quality of each prefix.
		double[] prefixEstimationArray = new double[myTarget.length+1];
		prefixEstimationArray[0] = 0.0;

		//Go in the loop to caliculate information estimation of all of the letters
		for(int n=1;n<=myTarget.length;n++) {
			double value = Double.MAX_VALUE;
			for(int start=n-1;start>=0;start--) {
				int freq = myFrequencer.subByteFrequency(start, n);
				//if the frequency is 0, break this loop.
				if(freq != 0) {
					//"value" always decreases to find the minimum value.
					double value1 = prefixEstimationArray[start]+iq(freq);
					if(value>value1)
						value = value1;
				}else{
					break;
				}
			}
		//save "value" to prefixEstimationArray[n]
		prefixEstimationArray[n]=value;
		}
	//return the latest minimum value
	return prefixEstimationArray[myTarget.length];
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
			       

	
    
