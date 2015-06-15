package com.example.mydemos.app;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.example.mydemos.R;

import android.app.Activity;
import android.os.Bundle;


/**
 * Simple example of writing an application Activity.
 * Hello World</a></h3>

<p>This demonstrates the basic code needed to write a Screen activity.</p>

<h4>Demo</h4>
App/Activity/Hello World
 
<h4>Source files</h4>
 * <table class="LinkTable">
 *         <tr>
 *             <td >src/com.example.android.apis/app/HelloWorld.java</td>
 *             <td >The Hello World Screen implementation</td>
 *         </tr>
 *         <tr>
 *             <td >/res/any/layout/hello_world.xml</td>
 *             <td >Defines contents of the screen</td>
 *         </tr>
 * </table> 
 */
public class HelloWorld extends Activity
{
    /**
     * Initialization of the Activity after it is first created.  Must at least
     * call {@link android.app.Activity#setContentView setContentView()} to
     * describe what is to be displayed in the screen.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);

        // See assets/res/any/layout/hello_world.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.hello_world);
    }
}