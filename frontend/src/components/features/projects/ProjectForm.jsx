import { projectColors, statusOptions, visibilityOptions } from '/src/data/project/constants.jsx';
import ColorPicker from '/src/components/ui/project/ColorPicker.jsx';
import TextInput from '/src/components/ui/project/TextInput.jsx';
import DateRangePicker from '/src/components/ui/project/DateRangePicker.jsx';
import SelectField from '/src/components/ui/project/SelectField.jsx';
import VisibilityOptionCard from '/src/components/ui/project/VisibilityOptionCard.jsx';
import StickyFooter from '/src/components/ui/project/StickyFooter.jsx';

const ProjectForm = ({ form, onSubmit, loading }) => {
  const { formData, errors, setField, validate } = form;

  const handleSubmit = () => {
    if (!validate()) return;
    const payload = {
      colorId: Number(formData.colorId),
      name: formData.name,
      status: formData.status,
      period: { startDate: formData.startDate, endDate: formData.endDate },
      description: formData.description || null,
      isPublic: formData.isPublic,
      visibility: formData.visibility,
    };
    onSubmit?.(payload);
  };

  return (
    <>
      <main className="p-6 pb-24">
        <ColorPicker
          colors={projectColors}
          value={formData.colorId}
          onChange={(id) => setField('colorId', id)}
        />

        <TextInput
          label="Title"
          value={formData.name}
          onChange={(v) => setField('name', v)}
          placeholder="프로젝트의 제목을 입력해주세요"
          error={errors.name}
        />

        <DateRangePicker
          start={formData.startDate}
          end={formData.endDate}
          onChangeStart={(v) => setField('startDate', v)}
          onChangeEnd={(v) => setField('endDate', v)}
          errors={errors}
        />

        <div className="mb-8">
          <label className="block text-sm text-gray-500 mb-3 uppercase tracking-wide">Description</label>
          <textarea
            value={formData.description}
            onChange={(e) => setField('description', e.target.value)}
            placeholder="프로젝트에 대한 설명을 입력하세요"
            className="w-full text-gray-700 resize-none border-none outline-none bg-transparent h-24 text-lg leading-relaxed placeholder-gray-400"
          />
        </div>

        <SelectField
          label="Status"
          value={formData.status}
          onChange={(v) => setField('status', v)}
          options={statusOptions}
        />

        <div className="mb-8">
          <label className="block text-sm text-gray-500 mb-4 uppercase tracking-wide">Visibility</label>
          <div className="space-y-3">
            {visibilityOptions.map(opt => (
              <VisibilityOptionCard
                key={opt.value}
                active={formData.visibility === opt.value}
                label={opt.label}
                description={opt.description}
                onClick={() => {
                  setField('visibility', opt.value);
                  setField('isPublic', opt.value === 'PUBLIC');
                }}
              />
            ))}
          </div>
        </div>
      </main>

      <StickyFooter>
        <button
          onClick={handleSubmit}
          disabled={loading}
          className={`w-full py-4 rounded-2xl font-bold text-lg transition-all ${
            loading
              ? 'bg-gray-400 cursor-not-allowed'
              : 'bg-gradient-to-r from-blue-600 to-purple-600 hover:shadow-xl active:scale-95'
          } text-white`}
        >
          {loading ? '생성 중...' : 'Create New Task'}
        </button>
      </StickyFooter>
    </>
  );
};

export default ProjectForm;