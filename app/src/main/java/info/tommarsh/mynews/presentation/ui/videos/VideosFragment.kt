package info.tommarsh.mynews.presentation.ui.videos

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import info.tommarsh.mynews.core.model.NetworkException
import info.tommarsh.mynews.core.util.snack
import info.tommarsh.mynews.presentation.di.HomeComponentProvider
import info.tommarsh.mynews.presentation.model.PlaylistItemViewModel
import info.tommarsh.mynews.presentation.ui.ArticleFragment
import info.tommarsh.presentation.R
import kotlinx.android.synthetic.main.fragment_videos.*

class VideosFragment : ArticleFragment() {

    private val adapter = VideosAdapter()

    private val viewModel: VideosViewModel by lazy {
        ViewModelProviders.of(this, factory).get(VideosViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as HomeComponentProvider)
            .homeComponent()
            .inject(this)
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
        viewModel.videos.observe(viewLifecycleOwner, Observer(::onVideos))
        viewModel.errors.observe(viewLifecycleOwner, Observer(::onError))

        viewModel.refreshVideos()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.videos_menu, menu)
    }

    private fun onVideos(videos: List<PlaylistItemViewModel>) {
        refresh_video.isRefreshing = false
        adapter.items = videos
    }

    private fun onError(error: NetworkException) {
        refresh_video.isRefreshing = false
        refresh_video.snack(error.localizedMessage)
    }

    private fun setLayoutManager() = GridLayoutManager(context, 2)
}