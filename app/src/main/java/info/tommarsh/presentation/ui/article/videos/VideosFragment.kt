package info.tommarsh.presentation.ui.article.videos

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import info.tommarsh.core.network.NetworkException
import info.tommarsh.presentation.NewsApp.Companion.graph
import info.tommarsh.presentation.R
import info.tommarsh.presentation.model.PlaylistItemViewModel
import info.tommarsh.presentation.ui.article.videos.adapter.VideosAdapter
import info.tommarsh.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment : BaseFragment() {

    private val adapter = VideosAdapter()

    private val viewModel: VideosViewModel by lazy {
        ViewModelProviders.of(this, factory).get(VideosViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graph(context!!).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_videos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videos_recycler_view.adapter = adapter
        videos_recycler_view.layoutManager = setLayoutManager()
        refresh_video.setOnRefreshListener {
            refresh_video.isRefreshing = true
            viewModel.refreshVideos()
        }
        refresh_video.isRefreshing = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getVideos().observe(viewLifecycleOwner, Observer(::onVideos))
        viewModel.getErrors().observe(viewLifecycleOwner, Observer(::onError))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.default_toolbar_menu, menu)
    }

    private fun onVideos(videos: List<PlaylistItemViewModel>) {
        refresh_video.isRefreshing = false
        adapter.submitList(videos)
    }

    private fun onError(error: NetworkException) {
        refresh_video.isRefreshing = false
        videos_root.snack(error.localizedMessage)
    }

    private fun setLayoutManager() = GridLayoutManager(context, 2)
}