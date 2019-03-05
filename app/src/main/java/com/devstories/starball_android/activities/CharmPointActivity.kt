package com.devstories.starball_android.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devstories.starball_android.R
import com.devstories.starball_android.base.RootActivity
import com.devstories.starball_android.base.Utils
import kotlinx.android.synthetic.main.activity_charm_point.*

class CharmPointActivity : RootActivity() {

    lateinit var context: Context
    private var progressDialog: ProgressDialog? = null


    var adapterData = ArrayList<String>()
    var type = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charm_point)
        this.context = this
        progressDialog = ProgressDialog(context)

        type = intent.getIntExtra("type", -1)

        if (type == 2) {
            titleTV.text = getString(R.string.meet_some_one)
        }



        backIV.setOnClickListener {
            val intent = Intent()
            intent.putExtra("charmPoint", adapterData)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        nextTV.setOnClickListener {
            val intent = Intent()
            Log.d("어뎀터", adapterData.toString())
            intent.putExtra("charmPoint", adapterData)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        itemclick()
    }


    fun itemclick() {
        item1TV.setOnClickListener {


            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item1TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                item1TV.setTextColor(Color.parseColor("#ffffff"))
                adapterData.add(Utils.getString(item1TV))
                Log.d("데터", adapterData.toString())
            } else {
                item1TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                item1TV.setTextColor(Color.parseColor("#555555"))
                adapterData.remove(Utils.getString(item1TV))
                Log.d("데터", adapterData.toString())
            }
        }
        item2TV.setOnClickListener {

            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item2TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                item2TV.setTextColor(Color.parseColor("#ffffff"))
                adapterData.add(Utils.getString(item2TV))
                Log.d("데터", adapterData.toString())
            } else {
                item2TV.setTextColor(Color.parseColor("#555555"))
                item2TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item2TV))
                Log.d("데터", adapterData.toString())
            }
        }
        item3TV.setOnClickListener {

            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item3TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item3TV))
                item3TV.setTextColor(Color.parseColor("#ffffff"))
                Log.d("데터", adapterData.toString())
            } else {
                item3TV.setTextColor(Color.parseColor("#555555"))
                item3TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item3TV))
                Log.d("데터", adapterData.toString())
            }
        }
        item4TV.setOnClickListener {


            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item4TV.setTextColor(Color.parseColor("#ffffff"))
                item4TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item4TV))
            } else {
                item4TV.setTextColor(Color.parseColor("#555555"))
                item4TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item4TV))
            }
        }
        item5TV.setOnClickListener {


            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item5TV.setTextColor(Color.parseColor("#ffffff"))
                item5TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item5TV))
            } else {
                item5TV.setTextColor(Color.parseColor("#555555"))
                item5TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item5TV))
            }
        }
        item6TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item6TV.setTextColor(Color.parseColor("#ffffff"))
                item6TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item6TV))
            } else {
                item6TV.setTextColor(Color.parseColor("#555555"))
                item6TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item6TV))
            }
        }
        item7TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item7TV.setTextColor(Color.parseColor("#ffffff"))
                item7TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item7TV))
            } else {
                item7TV.setTextColor(Color.parseColor("#555555"))
                item7TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item7TV))
            }
        }
        item8TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item8TV.setTextColor(Color.parseColor("#ffffff"))
                item8TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item8TV))
            } else {
                item8TV.setTextColor(Color.parseColor("#555555"))
                item8TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item8TV))
            }
        }
        item9TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item9TV.setTextColor(Color.parseColor("#ffffff"))
                item9TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item9TV))
            } else {
                item9TV.setTextColor(Color.parseColor("#555555"))
                item9TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item9TV))
            }
        }
        item10TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item10TV.setTextColor(Color.parseColor("#ffffff"))
                item10TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item10TV))
            } else {
                item10TV.setTextColor(Color.parseColor("#555555"))
                item10TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item10TV))
            }
        }
        item11TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item11TV.setTextColor(Color.parseColor("#ffffff"))
                item11TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item11TV))
            } else {
                item11TV.setTextColor(Color.parseColor("#555555"))
                item11TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item11TV))
            }
        }
        item12TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item12TV.setTextColor(Color.parseColor("#ffffff"))
                item12TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item12TV))
            } else {
                item12TV.setTextColor(Color.parseColor("#555555"))
                item12TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item12TV))
            }
        }
        item13TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item13TV.setTextColor(Color.parseColor("#ffffff"))
                item13TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item13TV))
            } else {
                item13TV.setTextColor(Color.parseColor("#555555"))
                item13TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item13TV))
            }
        }
        item14TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item14TV.setTextColor(Color.parseColor("#ffffff"))
                item14TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item6TV))
            } else {
                item14TV.setTextColor(Color.parseColor("#555555"))
                item14TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item14TV))
            }
        }
        item15TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item15TV.setTextColor(Color.parseColor("#ffffff"))
                item15TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item15TV))
            } else {
                item15TV.setTextColor(Color.parseColor("#555555"))
                item15TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item15TV))
            }
        }
        item16TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item16TV.setTextColor(Color.parseColor("#ffffff"))
                item16TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item16TV))
            } else {
                item16TV.setTextColor(Color.parseColor("#555555"))
                item16TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item16TV))
            }
        }
        item17TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item17TV.setTextColor(Color.parseColor("#ffffff"))
                item17TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item17TV))
            } else {
                item17TV.setTextColor(Color.parseColor("#555555"))
                item17TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item17TV))
            }
        }
        item18TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item18TV.setTextColor(Color.parseColor("#ffffff"))
                item18TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item18TV))
            } else {
                item18TV.setTextColor(Color.parseColor("#555555"))
                item18TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item18TV))
            }
        }
        item19TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item19TV.setTextColor(Color.parseColor("#ffffff"))
                item19TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item19TV))
            } else {
                item19TV.setTextColor(Color.parseColor("#555555"))
                item19TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item19TV))
            }
        }
        item20TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item20TV.setTextColor(Color.parseColor("#ffffff"))
                item20TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item20TV))
            } else {
                item20TV.setTextColor(Color.parseColor("#555555"))
                item20TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item20TV))
            }
        }
        item21TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item21TV.setTextColor(Color.parseColor("#ffffff"))
                item21TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item21TV))
            } else {
                item21TV.setTextColor(Color.parseColor("#555555"))
                item21TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item21TV))
            }
        }
        item22TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item22TV.setTextColor(Color.parseColor("#ffffff"))
                item22TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item22TV))
            } else {
                item22TV.setTextColor(Color.parseColor("#555555"))
                item22TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item22TV))
            }
        }
        item23TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item23TV.setTextColor(Color.parseColor("#ffffff"))
                item23TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item23TV))
            } else {
                item23TV.setTextColor(Color.parseColor("#555555"))
                item23TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item23TV))
            }
        }
        item24TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item24TV.setTextColor(Color.parseColor("#ffffff"))
                item24TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item6TV))
            } else {
                item24TV.setTextColor(Color.parseColor("#555555"))
                item24TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item24TV))
            }
        }
        item25TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item25TV.setTextColor(Color.parseColor("#ffffff"))
                item25TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item25TV))
            } else {
                item25TV.setTextColor(Color.parseColor("#555555"))
                item25TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item25TV))
            }
        }
        item26TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item26TV.setTextColor(Color.parseColor("#ffffff"))
                item26TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item26TV))
            } else {
                item26TV.setTextColor(Color.parseColor("#555555"))
                item26TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item26TV))
            }
        }
        item27TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item27TV.setTextColor(Color.parseColor("#ffffff"))
                item27TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item27TV))
            } else {
                item27TV.setTextColor(Color.parseColor("#555555"))
                item27TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item27TV))
            }
        }
        item28TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item28TV.setTextColor(Color.parseColor("#ffffff"))
                item28TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item28TV))
            } else {
                item28TV.setTextColor(Color.parseColor("#555555"))
                item28TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item28TV))
            }
        }
        item29TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item29TV.setTextColor(Color.parseColor("#ffffff"))
                item29TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item29TV))
            } else {
                item29TV.setTextColor(Color.parseColor("#555555"))
                item29TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item29TV))
            }
        }
        item30TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item30TV.setTextColor(Color.parseColor("#ffffff"))
                item30TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item30TV))
            } else {
                item30TV.setTextColor(Color.parseColor("#555555"))
                item30TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item30TV))
            }
        }
        item31TV.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                if (adapterData.count() > 3) {
                    Toast.makeText(context, "더이상 선택할수없습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                item31TV.setTextColor(Color.parseColor("#ffffff"))
                item31TV.setBackgroundResource(R.drawable.background_border_strock4_923b9f)
                adapterData.add(Utils.getString(item31TV))
            } else {
                item31TV.setTextColor(Color.parseColor("#555555"))
                item31TV.setBackgroundResource(R.drawable.background_border_strock4_d4d4d4)
                adapterData.remove(Utils.getString(item31TV))
            }
        }


    }



}
