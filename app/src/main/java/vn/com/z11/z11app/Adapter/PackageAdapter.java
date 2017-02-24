package vn.com.z11.z11app.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.ApiResponseModel.Code_StatusModel;
import vn.com.z11.z11app.ApiResponseModel.ErroResponse;
import vn.com.z11.z11app.ApiResponseModel.ListPackageResponse;
import vn.com.z11.z11app.ApiResponseModel.LoginResponse;
import vn.com.z11.z11app.ApiResponseModel.MyRateResponseModel;
import vn.com.z11.z11app.ChapterActibity;
import vn.com.z11.z11app.Database.Query.LocalDb;
import vn.com.z11.z11app.LoginActivity;
import vn.com.z11.z11app.MainActivity;
import vn.com.z11.z11app.R;
import vn.com.z11.z11app.RestAPI.ApiLession;
import vn.com.z11.z11app.RestAPI.ApiPurchases;
import vn.com.z11.z11app.RestAPI.ApiRate;
import vn.com.z11.z11app.RestAPI.ErrorUtils;
import vn.com.z11.z11app.Utilities.CommonMethod;

/**
 * Created by kienlv58 on 12/6/16.
 */
public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CategoryResponse.Packages> listpackage;
    ArrayList<ListPackageResponse.Packages> listLession;
    Context context;
    LayoutInflater inflater;
    CommonMethod commonMethod = new CommonMethod();
    LocalDb sql_user;
    LoginResponse.Metadata.User user;
    float mRate;


    public PackageAdapter(ArrayList<CategoryResponse.Packages> listpackage, ArrayList<ListPackageResponse.Packages> listLession, Context context) {
        this.listpackage = listpackage;
        this.listLession = listLession;
        sql_user = new LocalDb(context);
        user = sql_user.getUser();
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.package_item, parent, false);
        return new PackageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (listpackage != null) {
            final CategoryResponse.Packages itemPackage = listpackage.get(position);
            final PackageViewHolder myHolder = (PackageViewHolder) holder;
            myHolder.txtv_title.setText(itemPackage.translate_name_text.get(0).text_value + "");
            myHolder.txtv_description.setText(itemPackage.translate_describe_text.get(0).text_value);
            myHolder.txtv_price.setText(itemPackage.package_cost + "");
            myHolder.rate.setRating(itemPackage.balance_rate);
            myHolder.btn_unlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user == null) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", "other");
                        context.startActivity(intent);
                    } else {
                        unlock(itemPackage);
                    }
                }
            });

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (user == null) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", "other");
                        context.startActivity(intent);
                    } else {
                        checkPurchase(itemPackage, myHolder);
                    }

                }
            });
        } else if (listLession != null) {

        }

    }

    @Override
    public int getItemCount() {
        if (listLession == null) {
            return listpackage.size();
        } else {
            return listLession.size();
        }
    }

    public class PackageViewHolder extends RecyclerView.ViewHolder {
        TextView txtv_title;
        TextView txtv_description;
        TextView txtv_price;
        Button btn_unlock;
        RatingBar rate;

        public PackageViewHolder(View itemView) {
            super(itemView);
            txtv_title = (TextView) itemView.findViewById(R.id.txtv_title);
            txtv_description = (TextView) itemView.findViewById(R.id.txtv_description);
            txtv_price = (TextView) itemView.findViewById(R.id.txtv_price);
            btn_unlock = (Button) itemView.findViewById(R.id.btn_unlock);
            rate = (RatingBar) itemView.findViewById(R.id.rating_bar);

        }
    }

    public void unlock(final CategoryResponse.Packages itemPackage) {
        AlertDialog.Builder exit = new AlertDialog.Builder(context);
        exit.setMessage("Bạn muốn mua package này với " + itemPackage.package_cost + " xu ?");
        exit.setNegativeButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                purchasePackage(itemPackage, null);

            }
        });

        exit.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        exit.setCancelable(false);

        AlertDialog dialog = exit.create();
        dialog.show();
    }

    public void purchasePackage(final CategoryResponse.Packages itemPackage1, final ListPackageResponse.Packages itemPackage2) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();

        //send payment to server
        ApiPurchases apiUserAnswer = RestAdapter.getClient().create(ApiPurchases.class);
        Call<Code_StatusModel> payment = null;
        if (itemPackage1 != null) {
            payment = apiUserAnswer.addPurchase(itemPackage1.package_id, itemPackage1.item_code);
        } else if (itemPackage2 != null) {
            payment = apiUserAnswer.addPurchase(itemPackage2.package_id, itemPackage2.item_code);
        }
        if (payment != null)
            payment.enqueue(new Callback<Code_StatusModel>() {
                @Override
                public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {
                    int code = response.code();
                    if (code == 200) {
                        Code_StatusModel rsp = response.body();
                        progressDialog.dismiss();
                        commonMethod.showdiaglog(context, "Bạn đã mua thành công", "OK", null);

                    } else if (code == 400) {
                        ErroResponse err = ErrorUtils.parseError(response);
                        if (err.getStatus().equals("item exists")) {
                            progressDialog.dismiss();
                            commonMethod.showdiaglog(context, "Bạn đã mua pạckage này rồi", "OK", null);
                        } else if (err.getStatus().equals("coin enought pay to package")) {
                            progressDialog.dismiss();
                            commonMethod.showdiaglog(context, "Bạn không đủ xu để mua package này. Vui lòng nạp thêm", "OK", null);
                        } else if (err.getNewToken() != null) {
                            String token = "Bearer {" + err.getNewToken().toString() + "}";
                            MainActivity.localDb.updateToken(token);
                            RestAdapter.MyauthHeaderContent = token;
                            purchasePackage(itemPackage1, itemPackage2);

                        }
                    } else if (code == 401) {
                        MainActivity.localDb.deleteUser();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("from", "other");
                        context.startActivity(intent);
                        Toast.makeText(context, "het phien lam viec", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Code_StatusModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, t + "", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void checkPurchase(final CategoryResponse.Packages itemPackage, final PackageViewHolder myHolder) {

        //check if user charged => next to chapter else =>next to package detail (rate layout)
        ApiPurchases apiPurchases = RestAdapter.getClient().create(ApiPurchases.class);
        Call<Code_StatusModel> resp = apiPurchases.getChargeItem("package", itemPackage.package_id);
        resp.enqueue(new Callback<Code_StatusModel>() {
            @Override
            public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {
                int code = response.code();
                if (code == 200) {
                    //if charged
                    Intent toChapter = new Intent(context, ChapterActibity.class);
                    toChapter.putExtra("package_id", itemPackage.package_id);
                    context.startActivity(toChapter);

                } else if (code == 400) {
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if (erroResponse.getStatus().equals("can not find purchase")) {
                        //if not charge
                        showdiaglogRate(itemPackage);
                    } else if (erroResponse.getNewToken() != null) {
                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                        MainActivity.localDb.updateToken(token);
                        RestAdapter.MyauthHeaderContent = token;
                        checkPurchase(itemPackage, myHolder);

                    }


                } else if (code == 401) {
                    MainActivity.localDb.deleteUser();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("from", "other");
                    context.startActivity(intent);
                    Toast.makeText(context, "het phien lam viec", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Code_StatusModel> call, Throwable t) {

            }
        });
        myHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showdiaglogRate(itemPackage);
                return false;
            }
        });
    }

    public void showdiaglogRate(final CategoryResponse.Packages itemPackage) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        final ApiRate apiRate = RestAdapter.getClient().create(ApiRate.class);
        Call<MyRateResponseModel> response = apiRate.getMyRate(itemPackage.folder_id, itemPackage.package_id);
        response.enqueue(new Callback<MyRateResponseModel>() {
            @Override
            public void onResponse(Call<MyRateResponseModel> call, Response<MyRateResponseModel> response) {

                int code = response.code();
                if (code == 200) {
                    final MyRateResponseModel result = response.body();

                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
                    View v = inflater.inflate(R.layout.layout_rate, null);
                    TextView txtv_name, txtv_email, txtv_title, txtv_description, txtv_price;
                    RatingBar rtb_balance, rtb_myRate;
                    Button btn_rate, btn_unlock, btn_add;

                    CircleImageView avatar = (CircleImageView) v.findViewById(R.id.img_avt);
                    txtv_name = (TextView) v.findViewById(R.id.txtv_name);
                    txtv_email = (TextView) v.findViewById(R.id.txtv_email);
                    txtv_title = (TextView) v.findViewById(R.id.txtv_title);
                    txtv_description = (TextView) v.findViewById(R.id.txtv_description);
                    txtv_price = (TextView) v.findViewById(R.id.txtv_price);
                    rtb_balance = (RatingBar) v.findViewById(R.id.rtb_balance);
                    btn_rate = (Button) v.findViewById(R.id.btn_rate);
                    btn_unlock = (Button) v.findViewById(R.id.btn_unlock);
                    btn_add = (Button) v.findViewById(R.id.btn_addlesstion);
                    //myrate view
                    ImageView star1 = (ImageView) v.findViewById(R.id.img_star1);
                    ImageView star2 = (ImageView) v.findViewById(R.id.img_star2);
                    ImageView star3 = (ImageView) v.findViewById(R.id.img_star3);
                    ImageView star4 = (ImageView) v.findViewById(R.id.img_star4);
                    ImageView star5 = (ImageView) v.findViewById(R.id.img_star5);
                    final ArrayList<ImageView> listStar = new ArrayList<ImageView>();
                    listStar.add(star1);
                    listStar.add(star2);
                    listStar.add(star3);
                    listStar.add(star4);
                    listStar.add(star5);
                    if (result.my_rate != "0") {
                        float current_rate = Float.parseFloat(result.my_rate);
                        int numRate = (int) current_rate;
                        for (int i = 0; i < numRate; i++) {
                            listStar.get(i).setImageResource(R.drawable.star);
                        }
                    }
                    mRate = Float.parseFloat(result.my_rate);
                    //setonclick star
                    star1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRate = clickStar(view, listStar);
                        }
                    });
                    star2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRate = clickStar(view, listStar);
                        }
                    });
                    star3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRate = clickStar(view, listStar);
                        }
                    });
                    star4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRate = clickStar(view, listStar);
                        }
                    });
                    star5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRate = clickStar(view, listStar);
                        }
                    });


                    rtb_balance.setRating(itemPackage.balance_rate);
                    txtv_name.setText(result.name.toString());
                    txtv_email.setText(result.email.toString());
                    txtv_price.setText("Price:" + itemPackage.package_cost);
                    if (result.avatar != null)
                        Picasso.with(context).load(result.avatar.toString()).into(avatar);
                    txtv_title.setText(itemPackage.translate_name_text.get(0).text_value.toString());
                    txtv_description.setText(itemPackage.translate_describe_text.get(0).text_value.toString());

                    btn_unlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unlock(itemPackage);
                        }
                    });
                    btn_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder exit = new AlertDialog.Builder(context);
                            exit.setMessage("Bạn muốn thêm package này vào mylession ? ");
                            exit.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //
                                    addLession(itemPackage);

                                }
                            });

                            exit.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = exit.create();
                            dialog.show();
                        }
                    });
                    btn_rate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //click rate
                            if (!(mRate == Float.parseFloat(result.my_rate)) && result.my_rate == "0") {
                                Call<Code_StatusModel> rsp_add = apiRate.addMyRate(itemPackage.package_id, mRate);
                                rsp_add.enqueue(new Callback<Code_StatusModel>() {
                                    @Override
                                    public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {
                                        //add new rate success
                                        int code = response.code();
                                        if (code == 200) {
                                            Toast.makeText(context, "add rate success", Toast.LENGTH_SHORT).show();
                                        } else if (code == 400) {
                                            ErroResponse erroResponse = ErrorUtils.parseError(response);
                                            if (erroResponse.getNewToken() != null) {
                                                String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                                                MainActivity.localDb.updateToken(token);
                                                RestAdapter.MyauthHeaderContent = token;


                                            }


                                        } else if (code == 401) {
                                            MainActivity.localDb.deleteUser();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.putExtra("from", "other");
                                            context.startActivity(intent);
                                            Toast.makeText(context, "het phien lam viec", Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onFailure(Call<Code_StatusModel> call, Throwable t) {

                                    }
                                });
                            } else if (!(mRate == Float.parseFloat(result.my_rate)) && result.my_rate != "0") {
                                //edit rate
                                Call<Code_StatusModel> rsp_edit = apiRate.editMyRate(itemPackage.package_id, mRate);
                                rsp_edit.enqueue(new Callback<Code_StatusModel>() {
                                    @Override
                                    public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {
                                        int code = response.code();
                                        if (code == 200) {
                                            Toast.makeText(context, "edit rate success", Toast.LENGTH_SHORT).show();
                                        } else if (code == 400) {
                                            ErroResponse erroResponse = ErrorUtils.parseError(response);
                                            if (erroResponse.getNewToken() != null) {
                                                String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                                                MainActivity.localDb.updateToken(token);
                                                RestAdapter.MyauthHeaderContent = token;


                                            }


                                        } else if (code == 401) {
                                            MainActivity.localDb.deleteUser();
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.putExtra("from", "other");
                                            context.startActivity(intent);
                                            Toast.makeText(context, "het phien lam viec", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Code_StatusModel> call, Throwable t) {

                                    }
                                });

                            }
                        }
                    });

                    aBuilder.setView(v);
                    AlertDialog alertDialog = aBuilder.create();
                    alertDialog.show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<MyRateResponseModel> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    public int clickStar(final View star, final ArrayList<ImageView> listImg) {
        final int[] my_rate = {0};

        int id = star.getId();
        Boolean below = false;
        for (int i = 0; i < listImg.size(); i++) {
            if (below == true) {
                listImg.get(i).setImageResource(R.drawable.star_black);
            } else {
                my_rate[0]++;
                listImg.get(i).setImageResource(R.drawable.star);
                if (id == listImg.get(i).getId()) {
                    below = true;
                }
            }
        }


        return my_rate[0];
    }

    public void addLession(CategoryResponse.Packages itemPackage) {
        ApiLession apiLession = RestAdapter.getClient().create(ApiLession.class);
        Call<Code_StatusModel> resp = apiLession.addMyLession(itemPackage.package_id);
        resp.enqueue(new Callback<Code_StatusModel>() {
            @Override
            public void onResponse(Call<Code_StatusModel> call, Response<Code_StatusModel> response) {
                int code = response.code();
                if (code == 200) {
                    Toast.makeText(context, "add success", Toast.LENGTH_SHORT).show();
                } else if (code == 400) {
                    ErroResponse erroResponse = ErrorUtils.parseError(response);
                    if (erroResponse.getNewToken() != null) {
                        String token = "Bearer {" + erroResponse.getNewToken().toString() + "}";
                        MainActivity.localDb.updateToken(token);
                        RestAdapter.MyauthHeaderContent = token;


                    } else if (erroResponse.getStatus().equals("lession exist")) {
                        Toast.makeText(context, "lession exist", Toast.LENGTH_SHORT).show();
                    }
                } else if (code == 401) {
                    MainActivity.localDb.deleteUser();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra("from", "other");
                    context.startActivity(intent);
                    Toast.makeText(context, "het phien lam viec", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Code_StatusModel> call, Throwable t) {
                Toast.makeText(context, "" + t, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
