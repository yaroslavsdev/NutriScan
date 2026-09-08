from sqlalchemy.orm import Session
from app import models

ALLERGEN_TRIGGERS = {
    "Молоко": ["молоко", "сливки"],
    "Орехи": ["орех", "миндаль", "фундук", "арахис"]
}


def update_allergen_triggers(db: Session):
    for name, triggers in ALLERGEN_TRIGGERS.items():
        allergen = db.query(models.Allergen).filter(models.Allergen.name == name).first()

        if not allergen:
            allergen = models.Allergen(name=name)
            db.add(allergen)
            db.flush()

        existing = db.query(models.AllergenTrigger).filter(
            models.AllergenTrigger.allergen_id == allergen.id
        ).all()
        existing_word = [row.trigger_word for row in existing]

        for word in triggers:
            if word not in existing_word:
                db.add(models.AllergenTrigger(allergen_id=allergen.id, trigger_word=word))

    db.commit()