package moshe.apps.shoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class Lists  extends Activity {
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int LIST_EDIT=1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lists_list);
        
        
       	/*
        try{	
	    	DBAdapter db1 = new DBAdapter(this);
	    	db1.open();
	    	db1.updateListItem(2,"every day list");
	    	db1.close();
		}
		catch(Exception e){
        	e.printStackTrace();        
        } 	
    	*/      
       
        ListView lv= (ListView)findViewById(R.id.listview);
       
     
        DBAdapter db = new DBAdapter(this);
        db.open();
       
        Cursor c=db.getAllLists();
        MyAdapter adapter = new MyAdapter(this, c);
        try{
          lv.setAdapter(adapter);
        }
        catch(Exception e){
        	e.printStackTrace();        
        }
        db.close();
        
        

        registerForContextMenu((Button)findViewById(R.id.btnAddNewList));


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stock_item_menu, menu);
    }
    
  
    
 
    //start
    private class MyAdapter extends ResourceCursorAdapter {

        public MyAdapter(Context context, Cursor cur) {
            super(context, R.layout.list_item, cur);
        }

        @Override
        public View newView(Context context, Cursor cur, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(R.layout.lists_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cur) {
        	TextView tvListNameText = (TextView)view.findViewById(R.id.itemName);
            Button btnGoToList= (Button)view.findViewById(R.id.btnGoToList);
        	
            tvListNameText.setText(cur.getString(cur.getColumnIndex(DBAdapter.KEY_LISTNAME)));
            
            
            
            registerForContextMenu((TextView)tvListNameText);
            //tv click
            /*
            tvListNameText.setOnClickListener( new View.OnClickListener() {

				@Override
				public void onClick(View v) {
	               	  Toast toast = Toast.makeText(v.getContext(),"hi" ,Toast.LENGTH_SHORT);
                	  toast.show();
					
				}  
            	
            	
            	
            	
            });
            */
            
            //button click
            btnGoToList.setOnClickListener( new View.OnClickListener() {  
                  public void onClick(View v) {  
                	 
                	Intent i = new Intent(v.getContext(), ShoppingListActivity.class);

                    
                    
                    ListView lv= (ListView)findViewById(R.id.listview);
                    int p  = lv.getPositionForView(v);
                    
                    Cursor c = (Cursor) (lv.getAdapter().getItem(p));
                    final long  rowID = c.getInt(c.getColumnIndex(DBAdapter.KEY_LISTID));
                    
                	i.putExtra(DBAdapter.KEY_LISTID, rowID);
                    startActivityForResult(i, (int) (long)rowID);
                    
                	 /*
                	int i=rowID;
                	  Toast toast = Toast.makeText(v.getContext(),String.valueOf(rowID) ,Toast.LENGTH_SHORT);
                	  toast.show();
                     */
                
                   }
                }); 
         
        }


    } 
    //end
    

 
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	int res=item.getItemId();
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch(res) {
        /*
            case DELETE_ID:                
                DBAdapter db = new DBAdapter(this);
                db.open();
                db.deleteItem(info.id);
                db.close();
                fillData();
                return true;
             */  
            case R.id.edit_item:
            	Intent i = new Intent(this, ListItemEdit.class);
            	i.putExtra(DBAdapter.KEY_ROWID, info.id);
                startActivityForResult(i, LIST_EDIT);
            	return true;
            case R.id.remove_item:
            	//DBAdapter db = new DBAdapter(this);
            	//Intent i = new Intent(this, ItemEdit.class);
            	//i.putExtra(DBAdapter.KEY_ROWID, info.id);
               // startActivityForResult(i, LIST_EDIT);
            	
            	return true;
            	
        }
        return super.onContextItemSelected(item);
    }
       
    
}
