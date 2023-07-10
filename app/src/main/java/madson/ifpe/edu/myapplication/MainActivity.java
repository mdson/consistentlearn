package madson.ifpe.edu.myapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView addRotina;
    private ImageButton imgBtnAddRotina;
    private ListView listaDeRotinas;
    private ArrayAdapter<NotificationList> adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<NotificationList> listaRotinas;
    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);

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

        listaRotinas = new ArrayList<>();
        listaDeRotinas = findViewById(R.id.lista);
        adapter = new AdapterRotinasPersonalizado(this, listaRotinas);
        listaDeRotinas.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                atualizarTela();
            }
        });
        atualizarTela();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Exibir a lista ao retomar a atividade
        atualizarTela();
    }

    private void atualizarTela() {
        // Obter novos dados da lista de rotinas
        List<NotificationList> novaListaRotinas = NotificationData.getNotifications();

        // Verificar se a lista nova está vazia
        if (novaListaRotinas.isEmpty()) {
            Toast.makeText(this, "A lista está vazia", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false); // Parar a animação de atualização
            return;
        }

        // Limpar a lista atual e adicionar os novos dados
        listaRotinas.clear();
        listaRotinas.addAll(novaListaRotinas);

        // Notificar o adaptador de que os dados foram alterados
        adapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false); // Parar a animação de atualização
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
        switch (item.getItemId()) {
            case R.id.nav_item1:
                Toast.makeText(this, "Item 1 selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item2:
                Toast.makeText(this, "Item 2 selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_item3:
                Toast.makeText(this, "Item 3 selecionado", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnDeslogar:
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    mAuth.signOut();
                } else {
                    Toast.makeText(MainActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    @Override
    public void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }
}