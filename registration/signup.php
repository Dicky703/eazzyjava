<?php

   if($_SERVER['REQUEST_METHOD']=='POST'){
  // echo $_SERVER["DOCUMENT_ROOT"];  // /home1/demonuts/public_html
//including the database connection file
       include_once("config.php");

        $first_name = $_POST['first_name'];
        $last_name = $_POST['last_name'];
 	$username = $_POST['username'];
        $email = $_POST['email'];
 	$password = $_POST['password'];
	 if($first_name == '' || $last_name == '' || $username == '' ||$email == '' || $password == ''){
	        echo json_encode(array( "status" => "false","message" => "Parameter missing!") );
	 }else{

	        $query= "SELECT * FROM users WHERE username='$username'";
	        $result= mysqli_query($con, $query);

	        if(mysqli_num_rows($result) > 0){
	           echo json_encode(array( "status" => "false","message" => "Username already exist!") );
	        }else{
		 	 $query = "INSERT INTO users (first_name,last_name,username,email,password) VALUES ('$first_name','$last_name','$username','$email','$password')";
			 if(mysqli_query($con,$query)){

			     $query= "SELECT * FROM users WHERE username='$username'";
	                     $result= mysqli_query($con, $query);
		             $emparray = array();
	                     if(mysqli_num_rows($result) > 0){
	                     while ($row = mysqli_fetch_assoc($result)) {
                                     $emparray[] = $row;
                                   }
	                     }
			    echo json_encode(array( "status" => "true","message" => "Successfully registered!" , "data" => $emparray) );
		 	 }else{
		 		 echo json_encode(array( "status" => "false","message" => "Error occured, please try again!") );
		 	}
	    }
	            mysqli_close($con);
	 }
     }else{
                        echo json_encode(array( "status" => "false","message" => "Error occured, please try again!") );
        }
 ?>
