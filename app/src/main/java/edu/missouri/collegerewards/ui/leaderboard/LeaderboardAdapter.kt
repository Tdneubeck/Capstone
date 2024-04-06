import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.missouri.collegerewards.R
import edu.missouri.collegerewards.ui.leaderboard.LeaderboardTile

class LeaderboardAdapter(private val leaderboardData: List<LeaderboardTile>, private val context: Context) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_tile_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tile = leaderboardData[position]
        holder.rankTextView.text = tile.rank.toString()
        holder.usernameTextView.text = tile.username
        holder.scoreTextView.text = tile.score.toString()
    }

    override fun getItemCount(): Int {
        return leaderboardData.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.rankTextView)
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val scoreTextView: TextView = itemView.findViewById(R.id.scoreTextView)
    }
}
