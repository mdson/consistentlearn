package madson.ifpe.edu.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView addRotina;
    private ImageButton imgBtnAddRotina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addRotina = findViewById(R.id.txtAddRotina);
        imgBtnAddRotina = findViewById(R.id.imgBtnAddRotina);

        addRotina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPaginaAdicionarRotina(v);
            }
        });
        imgBtnAddRotina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirPaginaAdicionarRotina(v);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Lidar com os cliques dos itens do menu
//        switch (item.getItemId()) {
//            case R.id.nav_item1:
//                Toast.makeText(this, "Item 1 selecionado", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_item2:
//                Toast.makeText(this, "Item 2 selecionado", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_item3:
//                Toast.makeText(this, "Item 3 selecionado", Toast.LENGTH_SHORT).show();
//                break;
//        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickExpandirMenu(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void abrirPaginaAdicionarRotina(View view) {
        Intent intent = new Intent(MainActivity.this, AdicionarRotinaActivity.class);
        startActivity(intent);
    }
}