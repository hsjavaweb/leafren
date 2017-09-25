

import java.util.Scanner;

public class  Myjava{

	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    int start = scanner .nextInt();
	    int [] nums = new int[3];
	    for(int i=0; i<3; i++){
	        nums[i] = scanner.nextInt();
	    }
	    int target = scanner .nextInt();
		System.out.println(groupSum(start,nums,target));
	}
	public static boolean groupSum(int start, int[] nums, int target) {
		if (start >= nums.length) return (target == 0);  
		if (groupSum(start + 1, nums, target - nums[start])) return true;
		if (groupSum(start + 1, nums, target)) return true;  
		 return false;
        }

}