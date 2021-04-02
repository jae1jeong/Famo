package com.softsquared.template.kotlin.config

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.softsquared.template.kotlin.util.LoadingDialog

// Fragment의 기본을 작성, 뷰 바인딩 활용
abstract class BaseFragment<B : ViewBinding>(
    private val bind: (View) -> B,
    @LayoutRes layoutResId: Int
) : Fragment(layoutResId) {
    private var _binding: B? = null
    lateinit var mLoadingDialog: LoadingDialog

    private var fragmentResume = false
    private var fragmentVisible = false
    private var fragmentOnCreated = false

    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bind(super.onCreateView(inflater, container, savedInstanceState)!!)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!fragmentResume && fragmentVisible && fragmentOnCreated){
            viewPagerApiRequest()
        }
    }

    fun showCustomToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context) {
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && isResumed){
            fragmentResume = true
            fragmentVisible = false
            fragmentOnCreated = true
            viewPagerApiRequest()
        }
        // fragment가 onCreated 됬을 경우
        else if(isVisibleToUser){
            fragmentResume = false
            fragmentVisible = true
            fragmentOnCreated = true
        }

        // fragment를 나갈 경우
        else if(!isVisibleToUser && fragmentOnCreated){
            fragmentVisible = false
            fragmentResume = false
        }
    }

    open fun viewPagerApiRequest(){
    }

}