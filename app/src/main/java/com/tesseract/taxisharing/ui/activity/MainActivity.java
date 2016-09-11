package com.tesseract.taxisharing.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.tesseract.taxisharing.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drawer();
    }
    public void Drawer() {
        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withDrawerWidthDp(200)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Navigation"),
                        new PrimaryDrawerItem().withName("Amit Ghosh"),
                        new PrimaryDrawerItem().withName("Login").withSetSelected(true).withIdentifier(2),
                        new PrimaryDrawerItem().withName("LogOut").withSetSelected(true).withIdentifier(1)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.equals(1)) {
                            startActivity(new Intent(MainActivity.this,MapsActivity.class));
                            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        }
                        if (drawerItem.equals(2)) {
                            startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                        }


                        return true;
                    }
                }).withDrawerGravity(Gravity.LEFT)
                .build();
        result.openDrawer();
        result.closeDrawer();
        result.isDrawerOpen();
    }
}
