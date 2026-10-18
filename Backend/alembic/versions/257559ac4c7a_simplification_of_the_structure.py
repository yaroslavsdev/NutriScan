"""simplification of the structure

Revision ID: 257559ac4c7a
Revises: 0e35564eb9f8
Create Date: 2026-10-18 20:16:04.258379

"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision: str = '257559ac4c7a'
down_revision: Union[str, Sequence[str], None] = '0e35564eb9f8'
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    """Upgrade schema."""
    op.drop_index("ix_users_id", table_name="users")
    op.drop_index("ix_products_id", table_name="products")
    op.drop_index("ix_scan_history_id", table_name="scan_history")
    op.drop_index("ix_allergens_id", table_name="allergens")
    op.drop_index("ix_food_diary_entries_id", table_name="food_diary_entries")

    op.drop_index("index_food_diary_user_time", table_name="food_diary_entries")
    op.create_index(
        "index_food_diary_user_date", "food_diary_entries", ["user_id", "entry_date"]
    )

    op.drop_table("user_allergens")
    op.create_table(
        "user_allergens",
        sa.Column("user_id", sa.Integer, sa.ForeignKey("users.id"), primary_key=True),
        sa.Column("allergen_id", sa.Integer, sa.ForeignKey("allergens.id"), primary_key=True),
    )

    op.drop_table("allergen_triggers")
    op.create_table(
        "allergen_triggers",
        sa.Column("allergen_id", sa.Integer, sa.ForeignKey("allergens.id"), primary_key=True),
        sa.Column("trigger_word", sa.String, primary_key=True),
    )


def downgrade() -> None:
    """Downgrade schema."""
    pass
